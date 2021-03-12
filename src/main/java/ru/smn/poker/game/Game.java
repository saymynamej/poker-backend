package ru.smn.poker.game;

import lombok.extern.slf4j.Slf4j;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StateType;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class Game {
    private final GameSettings gameSettings;
    private final Table table;
    protected boolean isEnable;

    public Game(GameSettings gameSettings, Table table) {
        this.gameSettings = gameSettings;
        this.table = table;
    }

    public Table getTable() {
        return this.table;
    }

    public List<PlayerEntity> getPlayers() {
        return this.table.getPlayers();
    }

    public boolean isEnable() {
        return isEnable;
    }

    public String getGameName() {
        return this.table.getGameName();
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void addPlayer(PlayerEntity player) {
        if (!this.getPlayers().contains(player)) {
            player.setActive(true);
            this.getPlayers().add(player);
        }
    }

    public boolean removePlayer(PlayerEntity player) {
        return this.getPlayers().remove(player);
    }

    public boolean removePlayer(String name) {
        final Optional<PlayerEntity> optionalPlayer = getPlayers().stream()
                .filter(player -> player.getName().equals(name))
                .findAny();

        if (optionalPlayer.isPresent()) {
            final PlayerEntity player = optionalPlayer.get();
            player.setStateType(StateType.LEAVE);
            return this.removePlayer(player);
        }
        return false;
    }


    public abstract void start();

    public abstract TableSettings getTableSettings();

}
