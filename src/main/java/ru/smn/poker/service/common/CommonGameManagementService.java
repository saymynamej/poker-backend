package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.Game;
import ru.smn.poker.game.Round;
import ru.smn.poker.game.holdem.HoldemGame;
import ru.smn.poker.game.holdem.HoldemRound;
import ru.smn.poker.service.GameManagementService;
import ru.smn.poker.service.OrderService;
import ru.smn.poker.service.WinnerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static ru.smn.poker.converter.GameConverter.*;
import static ru.smn.poker.converter.PlayerConverter.*;
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

    @Override
    public Game restoreGame(GameEntity gameEntity) {
        final List<Player> players = toDTOs(gameEntity.getPlayers());
        final Game game = createGame(
                players,
                gameEntity.getGameType(),
                orderService,
                gameEntity.getId(),
                gameEntity.getName()
        );
        startGame(game);

        if (checkGameName(gameEntity.getName())) {
            games.put(gameEntity.getName(), game);
        }
        return game;
    }


    @Override
    @Transactional
    public GameEntity saveGame(Game game) {
        final GameEntity gameEntity = gameService.saveGame(toEntity(game));
        game.getRound().setGameId(gameEntity.getId());
        return gameEntity;

    }


    public Game createGame(List<Player> players,
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
                gameSettings.getStartSmallBlindBet(),
                gameSettings.getStartBigBlindBet(),
                gameId
        );
        final Game game = new HoldemGame(
                gameSettings,
                round
        );

        log.info("game: " + gameName + " restored");
        return game;
    }

    @Override
    public synchronized Game createGame(
            List<Player> players,
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
    public void startGame(String gameName) {
        startGame(getGameByName(gameName));
    }

    @Override
    public void startGame(Game game) {
        runnableGames.computeIfAbsent(game, game2 -> {
            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(game2::start);
            return executorService;
        });
    }


    @Override
    public void stopGame(String gameName) {
        stopGame(getGameByName(gameName));
    }

    @Override
    public void stopGame(Game game) {
        runnableGames.computeIfPresent(game, (game1, executorService) -> {
            try {
                game.stop();
                executorService.shutdown();
                executorService.awaitTermination(1, TimeUnit.SECONDS);
                log.info("game was stopped:" + game.getGameName());
            } catch (InterruptedException e) {
                log.info("error when stop game: " + e.getMessage());
            }
            return executorService;
        });
    }

    @Override
    public void addListener(Runnable runnable) {
        executorForListeners.submit(runnable);
    }

    private boolean checkGameName(String gameName) {
        return !games.containsKey(gameName);
    }

    private Game getGameByName(String gameName) {
        return runnableGames.entrySet()
                .stream()
                .filter(pair -> pair.getKey().getGameName().equals(gameName))
                .findAny()
                .orElseThrow(() -> new RuntimeException("cannot find game"))
                .getKey();
    }
}
