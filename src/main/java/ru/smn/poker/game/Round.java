package ru.smn.poker.game;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.RoundEntity;

import java.util.List;

public interface Round {

    void start();

    String getGameName();

    void restore();

    RoundSettings getRoundSettings();

    List<PlayerEntity> getPlayers();

}
