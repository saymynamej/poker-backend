package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.holdem.HoldemGame;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.BroadCastService;
import ru.sm.poker.util.ThreadUtil;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class GameListeners {
    private final ExecutorService executorServiceForStart = Executors.newSingleThreadExecutor();
    private final ExecutorService executorServiceForClear = Executors.newSingleThreadExecutor();
    private final BroadCastService broadCastService;
    private final Map<String, Game> games;
    private final List<Player> players;

    @PostConstruct
    public void init() {
        enable();
        enableClearListener();
    }

    private void enable() {
        executorServiceForStart.submit(() -> {
            while (true) {
                ThreadUtil.sleep(1);
                if (players.size() >= 4) {
                    final List<Player> players = new ArrayList<>(this.players);
                    final HoldemGame game = new HoldemGame(getRandomGameName(), 9, players, broadCastService);
                    players.forEach(player -> player.setGameName(game.getName()));
                    games.put(getRandomGameName(), game);
                    log.info("game was started, because found 4 person");
                    game.start();
                }
            }
        });
    }

    private void enableClearListener() {
        executorServiceForClear.submit(() -> {
            while (true) {
                ThreadUtil.sleep(10);
                final List<Game> allEmptyGame = findAllEmptyGame();
                clearGame(allEmptyGame);
            }
        });
    }

    private void clearGame(List<Game> games) {
        if (games != null && !games.isEmpty()) {
            games.forEach(game -> this.games.remove(game.getName()));
        }
    }

    private List<Game> findAllEmptyGame() {
        return games
                .values()
                .stream()
                .filter(game -> game.getRoundSettings().getPlayers().size() == 0)
                .collect(Collectors.toList());
    }

    private String getRandomGameName() {
        return UUID.randomUUID().toString();
    }

}
