package ru.sm.poker.game;

import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;

import java.util.List;

public interface Round {

    void startRound();

    String getGameName();

    void reloadRound();

    HoldemRoundSettings getHoldemRoundSettings();

    List<Player> getPlayers();

}
