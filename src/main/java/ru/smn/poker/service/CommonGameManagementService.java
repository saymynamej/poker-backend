package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.converter.GameConverter;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.entities.*;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.enums.PlayerType;
import ru.smn.poker.game.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommonGameManagementService implements GameManagementService {
    private final Map<Game, ExecutorService> runnableGames;
    private final Map<String, Game> games;
    private final ExecutorService executorForListeners = Executors.newCachedThreadPool();
    private final Map<GameType, GameSettings> mapSettings;
    private final GameService gameService;
    private final OrderService orderService;
    private final WinnerService winnerService;
    private final PasswordEncoder passwordEncoder;
    private final RandomNameService randomNameService;

    @Override
    public void create(int countOfPlayers, long defaultChipsCount) {
        final List<PlayerEntity> players = new ArrayList<>();
        for (int i = 0; i < countOfPlayers; i++) {
            final PlayerEntity player = PlayerEntity.builder()
                    .name(String.valueOf(i))
                    .enable(true)
                    .settings(PlayerSettingsEntity.builder()
                            .timeBank(60L)
                            .playerType(PlayerType.ORDINARY)
                            .build())
                    .password(passwordEncoder.encode(String.valueOf(i)))
                    .build();

            final ChipsCountEntity chipsCountEntity = ChipsCountEntity.builder()
                    .count(defaultChipsCount)
                    .build();

            player.setChipsCount(chipsCountEntity);

            players.add(player);
        }

        create(players, GameType.HOLDEM_HU);
    }

    public void create(
            List<PlayerEntity> players,
            GameType gameType
    ) {
        final String randomGameName = randomNameService.getRandomName();

        final GameEntity gameEntity = GameEntity.builder()
                .name(randomGameName)
                .gameType(gameType)
                .players(players)
                .build();

        setChipsInGame(players, gameEntity);

        players.forEach(playerEntity -> playerEntity.setGame(gameEntity));

        final GameEntity gameEntityFromBase = gameService.saveGame(gameEntity);

        startGame(gameEntityFromBase);

    }


    public void restore(GameEntity gameEntity) {
        final Game game = create(gameEntity);

        final Optional<RoundEntity> isNotFinishedRound = gameEntity.getRounds()
                .stream()
                .filter(roundEntity -> !roundEntity.isFinished())
                .findFirst();

        if (isNotFinishedRound.isEmpty()) {
            startGame(game);
            return;
        }

        final RoundEntity roundEntity = isNotFinishedRound.get();

        final RoundSettings roundSettings = HoldemRoundSettings.builder()
                .roundId(roundEntity.getId())
                .players(gameEntity.getPlayers())
                .activePlayer(roundEntity.getActivePlayer())
                .bigBlind(roundEntity.getBigBlind())
                .smallBlind(roundEntity.getSmallBlind())
                .bigBlindBet(roundEntity.getBigBlindBet())
                .button(roundEntity.getButton())
                .flop(roundEntity.getF1() == null ? null : List.of(
                        roundEntity.getF1(),
                        roundEntity.getF2(),
                        roundEntity.getF3()))
                .gameId(gameEntity.getId())
                .gameName(gameEntity.getName())
                .isFinished(roundEntity.isFinished())
                .lastBet(roundEntity.getLastBet())
                .river(roundEntity.getRiver())
                .smallBlindBet(roundEntity.getSmallBlindBet())
                .stageType(roundEntity.getStageType())
                .tern(roundEntity.getTern())
                .fullHistory(null)
                .stageHistory(null)
                .isAfk(false)
                .bank(roundEntity.getBank())
                .build();

        runnableGames.computeIfAbsent(game, game2 -> {
            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> game.restore(roundSettings));
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return executorService;
        });
    }


    public void startGame(GameEntity gameEntity) {
        final Game generatedGame = create(gameEntity);
        games.put(generatedGame.getGameName(), generatedGame);
        startGame(generatedGame);
    }

    private void setChipsInGame(List<PlayerEntity> players, GameEntity gameEntity) {
        final List<ChipsCountEntity> chipsCountEntities = players.stream()
                .map(PlayerEntity::getChipsCount)
                .collect(Collectors.toList());
        gameEntity.setCounts(chipsCountEntities);

        chipsCountEntities.forEach(chipsCountEntity -> chipsCountEntity.setGame(gameEntity));
    }

    public void create(List<PlayerEntity> players, GameType gameType, OrderService orderService) {
        final Game game = create(
                players,
                gameType,
                orderService,
                0
        );

        if (checkGameName(game.getGameName())) {
            games.put(game.getGameName(), game);
            final GameEntity gameEntityFromBase = gameService.saveGame(GameConverter.toEntity(game));
            startGame(game);
            log.info("created new game:" + game.getGameName());
        }
    }


    public void create(GameType gameType, OrderService orderService) {
        final Game game = create(
                Collections.emptyList(),
                gameType,
                orderService,
                0
        );

        if (checkGameName(game.getGameName())) {
            games.put(game.getGameName(), game);
            gameService.saveGame(GameConverter.toEntity(game));
            startGame(game);
            log.info("created new game:" + game.getGameName());
        }

    }


    @Override
    public void startGame(Game game) {
        runnableGames.computeIfAbsent(game, game2 -> {
            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(game::start);
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return executorService;
        });
    }

    public Game create(GameEntity gameEntity) {
        final GameSettings gameSettings = mapSettings.get(gameEntity.getGameType());

        final Round round = new HoldemRound(
                gameEntity.getPlayers(),
                gameEntity.getName(),
                orderService,
                winnerService,
                gameService,
                gameSettings.getStartSmallBlindBet(),
                gameSettings.getStartBigBlindBet(),
                gameEntity.getId()
        );


        return new HoldemGame(
                gameSettings,
                round
        );
    }

    public Game create(List<PlayerEntity> players,
                       GameType gameType,
                       OrderService orderService,
                       long gameId,
                       String gameName
    ) {
        if (gameId == 0) {
            gameId = gameService.getNextGameId();
        }

        final GameSettings gameSettings = mapSettings.get(gameType);

        final Round round = new HoldemRound(
                new ArrayList<>(players),
                gameName,
                orderService,
                winnerService,
                gameService,
                gameSettings.getStartSmallBlindBet(),
                gameSettings.getStartBigBlindBet(),
                gameId
        );


        return new HoldemGame(
                gameSettings,
                round
        );
    }

    @Override
    public synchronized Game create(
            List<PlayerEntity> players,
            GameType gameType,
            OrderService orderService,
            long gameId
    ) {

        final String randomGameName = randomNameService.getRandomName();

        return create(
                players,
                gameType,
                orderService,
                gameId,
                randomGameName
        );
    }

    @Override
    public void addListener(Runnable runnable) {
        executorForListeners.submit(runnable);
    }

    private boolean checkGameName(String gameName) {
        return !games.containsKey(gameName);
    }

}
