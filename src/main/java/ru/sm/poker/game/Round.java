package ru.sm.poker.game;

import ru.sm.poker.model.RoundSettings;

public interface Round {

    void startRound();

    void reloadRound();

    RoundSettings getRoundSettings();

}
