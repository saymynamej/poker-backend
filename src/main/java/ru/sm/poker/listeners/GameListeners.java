package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.holdem.HoldemGame;
import ru.sm.poker.game.holdem.HoldemManager;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.BroadCastService;
import ru.sm.poker.util.ThreadUtil;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static ru.sm.poker.util.GameUtil.getRandomGameName;


@Component
@RequiredArgsConstructor
@Slf4j
public class GameListeners {

    private final ExecutorService executorServiceForStart = Executors.newFixedThreadPool(10);
    private final ExecutorService executorServiceForClear = Executors.newSingleThreadExecutor();
    private final BroadCastService broadCastService;
    private final ActionService actionService;
    private final HoldemManager holdemManager;
    private final Queue<Player> players;
    private boolean isEnable = true;

    @PostConstruct
    public void init() {
        enable();
        enableClearListener();
    }


    private void enable() {
        executorServiceForStart.submit(() -> {
            while (isEnable) {
                ThreadUtil.sleep(1);
                if (players.size() > 3) {
                    final String randomGameName = getRandomGameName();
                    final HoldemGame holdemGame = new HoldemGame(randomGameName, 9, extractQueue(), broadCastService, actionService);
                    holdemManager.createNewGame(randomGameName, holdemGame);
                    holdemGame.start();
                }
            }
        });
    }

    private List<Player> extractQueue() {
        final List<Player> players = new ArrayList<>();
        final int size = this.players.size();
        for (int i = 0; i < size; i++) {
            players.add(this.players.poll());
        }
        return players;
    }

    private void enableClearListener() {
        executorServiceForClear.submit(() -> {
            while (isEnable) {
                ThreadUtil.sleep(10);
                final List<Game> allEmptyGame = findAllEmptyGame();
                clearGame(allEmptyGame);
            }
        });
    }

    private void clearGame(List<Game> games) {
        if (games != null && !games.isEmpty()) {
            games.forEach(game -> holdemManager.getAllGames().remove(game.getName()));
        }
    }

    private List<Game> findAllEmptyGame() {
        return holdemManager.getAllGames()
                .values()
                .stream()
                .filter(game -> game.getRoundSettings().getPlayers().size() == 0)
                .collect(Collectors.toList());
    }
}
