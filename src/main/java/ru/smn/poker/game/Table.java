package ru.smn.poker.game;

import ru.smn.poker.entities.PlayerEntity;

import java.util.List;

public interface Table {

    void start();

    String getGameName();

    TableSettings getTableSettings();

    List<PlayerEntity> getPlayers();

}
