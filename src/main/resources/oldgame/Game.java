package ru.sm.poker.oldgame;

import ru.sm.poker.model.Player;
import ru.sm.poker.model.RoundSettings;
import ru.sm.poker.model.action.Bet;

import java.util.List;
import java.util.Optional;

public interface Game {

    void startGame();

    void reload();

}
