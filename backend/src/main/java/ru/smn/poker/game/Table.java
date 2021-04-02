package ru.smn.poker.game;

import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.PlayerEntity;

import java.util.List;

public interface Table {

    void start();

    String getGameName();

    GameSettings getGameSettings();

    TableSettings getTableSettings();

    List<PlayerEntity> getPlayers();

    void addPlayer(PlayerEntity playerEntity);

}
