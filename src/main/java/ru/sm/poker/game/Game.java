package ru.sm.poker.game;

import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.StateType;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class Game {
    private final GameSettings gameSettings;
    private final Round round;
    protected boolean isEnable;

    public Game(GameSettings gameSettings, Round round) {
        this.gameSettings = gameSettings;
        this.round = round;
    }

    protected Round getRound() {
        return this.round;
    }

    public List<Player> getPlayers() {
        return this.round.getPlayers();
    }

    public boolean isEnable() {
        return isEnable;
    }

    public String getGameName() {
        return this.round.getGameName();
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void addPlayer(Player player) {
        if (!this.getPlayers().contains(player)) {
            player.setActive();
            this.getPlayers().add(player);
        }
    }

    public boolean removePlayer(Player player) {
        return this.getPlayers().remove(player);
    }

    public boolean removePlayer(String name) {
        final Optional<Player> optionalPlayer = getPlayers().stream()
                .filter(player -> player.getName().equals(name))
                .findAny();

        if (optionalPlayer.isPresent()) {
            final Player player = optionalPlayer.get();
            player.setStateType(StateType.LEAVE);
            return this.removePlayer(player);
        }
        return false;
    }

    protected abstract boolean isReady();

    public abstract void start();

    public abstract void stop();

    public abstract HoldemRoundSettings getRoundSettings();

}
