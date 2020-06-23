package ru.sm.poker.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.game.Game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameManagement {
    private final Map<Game, ExecutorService> games = new ConcurrentHashMap<>();
    private final ExecutorService executorForListeners = Executors.newCachedThreadPool();

    public void startGame(Game game) {
        games.computeIfAbsent(game, game2 -> {
            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(game2::start);
            return executorService;
        });
    }

    public void stopGame(String gameName) {
        final Game game = games.entrySet()
                .stream()
                .filter(pair -> pair.getKey().getGameName().equals(gameName))
                .findAny()
                .orElseThrow(() -> new RuntimeException("cannot find game"))
                .getKey();

        stopGame(game);
    }

    public void stopGame(Game game) {
        games.computeIfPresent(game, (game1, executorService) -> {
            try {
                executorService.shutdown();
                executorService.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.info("error when stop game: " + e.getMessage());
            }
            return executorService;
        });
    }

    public void addListener(Runnable runnable) {
        executorForListeners.submit(runnable);
    }
}
