package ru.sm.poker.game.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.RoundSettings;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Bet;
import ru.sm.poker.service.BroadCastService;
import ru.sm.poker.util.ThreadUtil;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final static int MAX_PLAYERS = 9;
    private final static int MIN_PLAYER = 4;
    private boolean isRunning;
    private int bigBlindBet = 2;
    private int smallBlindBet = 1;
    private HoldemRound round;

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
    public boolean playerExistByName(String name) {
        return getPlayers()
                .stream()
                .anyMatch(exist -> exist.getName().equals(name));
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
    public Optional<Player> getPlayerByName(String name) {
        return players
                .stream()
                .filter(player -> player.getName().equals(name))
                .findFirst();
    }

    @Override
    public Player getActivePlayer() {
        return this.round.getActivePlayer();
    }

    @Override
    public Bet getLastBet() {
        return this.round.getLastBet();
    }

    @Override
    public RoundSettings getRoundSettings() {
        return RoundSettings.builder()
                .flop(this.round.getFlop())
                .tern(this.round.getTern())
                .river(this.round.getRiver())
                .activePlayer(this.round.getActivePlayer())
                .button(this.round.getPlayerByRole( RoleType.BUTTON))
                .bank(this.round.getBank())
                .smallBlind(this.round.getPlayerByRole(RoleType.SMALL_BLIND))
                .bigBlind(this.round.getPlayerByRole(RoleType.BIG_BLIND))
                .players(this.round.getAllPlayers())
                .build();
    }
}