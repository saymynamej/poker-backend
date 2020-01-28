package ru.sm.poker.game;

import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.Player;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class Game {

    private final GameSettings gameSettings;
    private final List<Player> players;
    private final Round round;
    protected boolean isEnable = true;

    public Game(GameSettings gameSettings, List<Player> players, Round round) {
        this.gameSettings = gameSettings;
        this.players = players;
        this.round = round;
    }

    protected Round getRound() {
        return this.round;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public String getName() {
        return this.gameSettings.getGameName();
    }

    public int getMaxPlayersSize() {
        return this.gameSettings.getMaxPlayerSize();
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void addPlayer(Player player) {
        if (!this.players.contains(player)) {
            player.setStateType(StateType.IN_GAME);
            this.players.add(player);
        }
    }

    public boolean removePlayer(Player player) {
        return this.players.remove(player);
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

    public abstract void start();

    public abstract void enable();

    public abstract void disable();

    public abstract void reload();

    public abstract HoldemRoundSettingsDTO getRoundSettings();

}
