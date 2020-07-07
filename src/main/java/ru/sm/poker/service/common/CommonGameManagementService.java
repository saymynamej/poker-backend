package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.converter.GameConverter;
import ru.sm.poker.converter.PlayerConverter;
import ru.sm.poker.dto.Player;
import ru.sm.poker.entities.GameEntity;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.Round;
import ru.sm.poker.game.holdem.HoldemGame;
import ru.sm.poker.game.holdem.HoldemRound;
import ru.sm.poker.service.GameManagementService;
import ru.sm.poker.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static ru.sm.poker.util.GameUtil.getRandomGOTCityName;

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

    @Override
    public Game restoreGame(GameEntity gameEntity) {
        final Game game = createGame(
                PlayerConverter.toDTOs(gameEntity.getPlayers()),
                gameEntity.getGameType(),
                orderService);
        startGame(game);
        return game;
    }


    @Override
    @Transactional
    public void saveGame(Game game) {
        gameService.saveGame(GameConverter.toEntity(game));
    }

    @Override
    public Game createGame(
            List<Player> players,
            GameType gameType,
            OrderService orderService
    ) {
        final String randomGameName = getRandomGOTCityName();

        final GameSettings gameSettings = mapSettings.get(gameType);

        final Round round = new HoldemRound(
                new ArrayList<>(players),
                randomGameName,
                orderService,
                gameSettings.getStartSmallBlindBet(),
                gameSettings.getStartBigBlindBet()
        );

        final Game game = new HoldemGame(
                gameSettings,
                round
        );

        if (checkGameName(randomGameName)) {
            games.put(randomGameName, game);
        }
        log.info("game: " + randomGameName + " restored");
        return game;
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
