package ru.sm.poker.game.holdem;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.Round;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.RoundSettings;
import ru.sm.poker.service.BroadCastService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HoldemGame implements Game {

    @Getter
    private final String name;

    @Getter
    private final int maxPlayersSize;

    private final List<Player> players;

    private final Round round;

    private boolean isStarted = false;

    @Builder
    public HoldemGame(String name, int maxPlayersSize, List<Player> players, BroadCastService broadCastService) {
        this.name = name;
        this.maxPlayersSize = maxPlayersSize;
        this.players = players;
        this.round = new HoldemRound(players, name, broadCastService);
    }

    @Override
    public void start() {
        while (players.size() >= 4 || !isStarted) {
            isStarted = true;
            round.startRound();
        }
    }

    @Override
    public void stop() {
        isStarted = false;
    }

    @Override
    public void reload() {

    }

    @Override
    public RoundSettings getRoundSettings() {
        return round.getRoundSettings();
    }

    @Override
    public void addPlayer(Player player) {
        players.add(player);
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player);
    }
}
