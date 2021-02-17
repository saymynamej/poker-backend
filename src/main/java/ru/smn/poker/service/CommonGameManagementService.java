package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.holdem.Wait;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.entities.*;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.enums.PlayerType;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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
    private final PrizeService prizeService;
    private final PasswordEncoder passwordEncoder;
    private final RandomNameService randomNameService;

    @Override
    public void create(int countOfPlayers, long defaultChipsCount, GameType gameType) {
        final String randomName = randomNameService.getRandomName();
        final List<PlayerEntity> players = IntStream.range(0, countOfPlayers).mapToObj(i -> PlayerEntity.builder()
                .name(String.valueOf(i))
                .enable(true)
                .settings(PlayerSettingsEntity.builder()
                        .timeBank(50000L)
                        .gameName(randomName)
                        .playerType(PlayerType.ORDINARY)
                        .build())
                .password(passwordEncoder.encode(String.valueOf(i)))
                .build()).collect(Collectors.toList());

        final Game game = create(defaultChipsCount, gameType, randomName, players);

        run(game);
    }


    @Transactional(readOnly = true)
    @Override
    public void restoreAll() {
        final List<GameEntity> games = gameService.findAll();

        for (GameEntity gameEntity : games) {
            final Optional<RoundEntity> isNotFinishedRound = gameEntity.getRounds()
                    .stream()
                    .filter(roundEntity -> !roundEntity.isFinished())
                    .findFirst();

            if (isNotFinishedRound.isEmpty()) {
                run(gameEntity);
                return;
            }

            final RoundEntity roundEntity = isNotFinishedRound.get();

            final Map<PlayerEntity, List<Action>> allActions = roundEntity.getActions().stream()
                    .collect(Collectors.groupingBy(ActionEntity::getPlayer)).entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
                            .map(actionEntity -> ActionType.getActionByType(actionEntity.getActionType(), actionEntity.getCount()))
                            .collect(Collectors.toList())));

            final Map<PlayerEntity, List<Action>> stageActions = roundEntity.getActions().stream()
                    .filter(actionEntity -> actionEntity.getStageType() == roundEntity.getStageType())
                    .collect(Collectors.groupingBy(ActionEntity::getPlayer)).entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
                            .map(actionEntity -> ActionType.getActionByType(actionEntity.getActionType(), actionEntity.getCount()))
                            .collect(Collectors.toList())));

            stageActions.forEach((player, action) -> {
                final boolean playerHasOnlyBlindBet = action.size() == 1 && roundEntity.getStageType() == StageType.PREFLOP;
                if (action.isEmpty() || playerHasOnlyBlindBet) {
                    player.setAction(new Wait());
                } else {
                    player.setAction(action.get(action.size() - 1));
                }
            });

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
                    .fullHistory(allActions)
                    .stageHistory(stageActions)
                    .isAfk(false)
                    .bank(roundEntity.getBank())
                    .build();

            final Game game = convert(gameEntity, roundSettings);

            run(game);
        }
    }


    @Override
    public void run(GameEntity gameEntity) {
        final Game generatedGame = convert(gameEntity);
        run(generatedGame);
    }

    @Override
    public void run(Game game) {
        saveInCache(game);
        runnableGames.computeIfAbsent(game, game2 -> {
            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            final boolean isRestore = game.getRoundSettings() != null;
            executorService.submit(game::start);
            if (isRestore) {
                log.info("game was restored: " + game.getGameName());
            } else {
                log.info("game was started: " + game.getGameName());
            }
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return executorService;
        });
    }

    @Override
    public void create(List<PlayerEntity> players, GameType gameType, long defaultChipsCount) {
        final Game game = create(
                defaultChipsCount,
                gameType,
                randomNameService.getRandomName(),
                players
        );

        run(game);
    }

    @Override
    public void createEmptyGame(GameType gameType) {
        final GameEntity emptyGame = GameEntity.builder()
                .gameType(gameType)
                .rounds(new ArrayList<>())
                .counts(new ArrayList<>())
                .players(new ArrayList<>())
                .name(randomNameService.getRandomName())
                .build();

        final GameEntity gameEntityFromBase = gameService.saveGame(emptyGame);

        run(gameEntityFromBase);
    }

    @Override
    public void addListener(Runnable runnable) {
        executorForListeners.submit(runnable);
    }

    private boolean checkGameName(String gameName) {
        return !games.containsKey(gameName);
    }

    private Game convert(GameEntity gameEntity, RoundSettings roundSettings) {
        final GameSettings gameSettings = mapSettings.get(gameEntity.getGameType());

        final Round round = new HoldemRound(
                gameEntity.getPlayers(),
                gameEntity.getName(),
                orderService,
                prizeService,
                gameService,
                gameSettings.getStartSmallBlindBet(),
                gameSettings.getStartBigBlindBet(),
                gameEntity.getId(),
                roundSettings
        );

        return new HoldemGame(
                gameSettings,
                round
        );

    }

    private Game convert(GameEntity gameEntity) {
        final GameSettings gameSettings = mapSettings.get(gameEntity.getGameType());

        final Round round = new HoldemRound(
                gameEntity.getPlayers(),
                gameEntity.getName(),
                orderService,
                prizeService,
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

    private Game create(long defaultChipsCount, GameType gameType, String randomName, List<PlayerEntity> players) {
        final GameEntity gameEntity = GameEntity.builder()
                .gameType(gameType)
                .name(randomName)
                .players(players)
                .counts(Collections.emptyList())
                .rounds(Collections.emptyList())
                .build();

        players.forEach(playerEntity -> {
            playerEntity.setGame(gameEntity);
            playerEntity.setChipsCount(ChipsCountEntity.builder()
                    .game(gameEntity)
                    .count(defaultChipsCount)
                    .build());
        });

        final GameEntity gameEntityFromBase = gameService.saveGame(gameEntity);

        return convert(gameEntityFromBase);
    }


    private void saveInCache(Game game) {
        if (checkGameName(game.getGameName())) {
            this.games.put(game.getGameName(), game);
            return;
        }
        throw new RuntimeException("duplicate game with name: " + game.getGameName());
    }
}
