package ru.smn.poker.game;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.RoundEntity;

import java.util.List;

public interface Round {

    void startRound();

    String getGameName();

    void restore(RoundSettings roundSettings);

    RoundSettings getRoundSettings();

    List<PlayerEntity> getPlayers();

    void setGameId(Long gameId);

}
