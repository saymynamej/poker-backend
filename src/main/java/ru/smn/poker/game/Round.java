package ru.smn.poker.game;

import ru.smn.poker.entities.PlayerEntity;

import java.util.List;

public interface Round {

    void startRound();

    String getGameName();

    void reloadRound();

    RoundSettings getRoundSettings();

    List<PlayerEntity> getPlayers();

    void setGameId(Long gameId);

}
