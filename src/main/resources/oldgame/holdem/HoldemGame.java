package ru.sm.poker.oldgame.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.oldgame.Game;
import ru.sm.poker.oldgame.Round;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.BroadCastService;
import ru.sm.poker.util.ThreadUtil;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
@Getter
public class HoldemGame implements Game {

    private final List<Player> players = new ArrayList<>();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final BroadCastService broadCastService;
    private int bigBlindBet = 2;
    private int smallBlindBet = 1;
    private HoldemRound round;
    private final static int MIN_PLAYER = 4;

    @PostConstruct
    public void init() {
        pushPlayers();
        startGame();
    }

    private void pushPlayers() {
        players.add(new Player("test2", 5000));
        players.add(new Player("test3", 5000));
        players.add(new Player("test4", 5000));
    }


    @Override
    public void startGame() {
        executorService.submit(() -> {
            while (true) {
                ThreadUtil.sleep(5);
                if (players.size() >= MIN_PLAYER) {
                    log.info("Game was started with size of players:" + getPlayers().size());
                    this.round = new HoldemRound(
                            players,
                            broadCastService,
                            smallBlindBet,
                            bigBlindBet);
                    this.round.startRound();
                } else {
                    log.info("Game not running, waiting players, now size:" + getPlayers().size());
                }
            }
        });
    }

    @Override
    public void reload() {
        try {
            if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                this.executorService.shutdownNow();
                this.executorService = Executors.newSingleThreadExecutor();
            }
            this.players.clear();
            pushPlayers();
            startGame();
            broadCastService.sendToAll(getPlayers());
        } catch (InterruptedException e) {
            System.out.println("cancel");
        }
    }


    List<Player> getPlayers() {
        return this.players;
    }

    void addPlayer(Player player) {
        this.players.add(player);
    }

}