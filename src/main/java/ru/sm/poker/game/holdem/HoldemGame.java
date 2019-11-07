package ru.sm.poker.game.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Bet;
import ru.sm.poker.util.ThreadUtil;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
@Slf4j
public class HoldemGame implements Game {
    @Getter
    private final List<Player> players = new ArrayList<>();
    private HoldemRound round;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final static int MAX_PLAYERS = 9;
    private final static int MIN_PLAYER = 4;
    private volatile boolean isRunning;
    private int bigBlindBet = 2;
    private int smallBlindBet = 1;

    @PostConstruct
    public void init(){
        startGame();
    }

    @Override
    public void startGame() {
        executorService.submit(() -> {
            while (true) {
                ThreadUtil.sleep(3);
                if (players.size() >= MIN_PLAYER) {
                    enable();
                    log.info("Game was started with size of players:" + getPlayers().size());
                    this.round = new HoldemRound(
                            players,
                            simpMessagingTemplate,
                            smallBlindBet,
                            bigBlindBet);
                    this.round.startRound();
                } else {
                    log.info("Game not running:" + getPlayers().size());
                    disable();
                }
            }
        });
    }

    private void enable() {
        this.isRunning = true;
    }

    private void disable() {
        this.isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public boolean addPlayer(Player player) {
        if (players.size() == MAX_PLAYERS ||
                players
                        .stream()
                        .anyMatch(pl -> pl.getName().equals(player.getName()))) {
         return false;
        }
        this.players.add(player);
        return true;
    }


    @Override
    public void addPlayers(List<Player> players) {
        players.forEach(this::addPlayer);
    }

    @Override
    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    @Override
    public Player getPlayerByName(String name) {
        return players
                .stream()
                .filter(player -> player.getName().contains(name))
                .findFirst()
                .orElseThrow(()-> new RuntimeException(format("cannot find player with name=%s", name)));
    }

    @Override
    public Player getActivePlayer() {
        return this.round.getActivePlayer();
    }

    @Override
    public Bet getLastBet() {
        return this.round.getLastBet();
    }
}