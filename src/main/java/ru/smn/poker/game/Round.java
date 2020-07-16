package ru.smn.poker.game;

import ru.smn.poker.dto.Player;

import java.util.List;

public interface Round {

    void startRound();

    String getGameName();

    void reloadRound();

    RoundSettings getHoldemRoundSettings();

    List<Player> getPlayers();

    void setGameId(Long gameId);

}
