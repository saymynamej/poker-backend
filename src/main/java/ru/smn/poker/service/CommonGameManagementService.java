package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.converter.GameConverter;
import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.Game;
import ru.smn.poker.game.Round;
import ru.smn.poker.game.HoldemGame;
import ru.smn.poker.game.HoldemRound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static ru.smn.poker.util.GameUtil.getRandomGOTCityName;


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


    public void createGame(
            List<PlayerEntity> players,
            GameType gameType
    ) {
        final String randomGameName = getRandomGOTCityName();

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


    public void startGame(GameEntity gameEntity) {
        final Game generatedGame = createGame(gameEntity);
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

    public void createGame(List<PlayerEntity> players, GameType gameType, OrderService orderService) {
        final Game game = createGame(
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


    public void createGame(GameType gameType, OrderService orderService) {
        final Game game = createGame(
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

    public Game createGame(GameEntity gameEntity) {
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

    public Game createGame(List<PlayerEntity> players,
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
    public synchronized Game createGame(
            List<PlayerEntity> players,
            GameType gameType,
            OrderService orderService,
            long gameId
    ) {

        final String randomGameName = getRandomGOTCityName();
        return createGame(
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
