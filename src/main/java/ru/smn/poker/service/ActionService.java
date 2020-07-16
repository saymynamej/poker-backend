package ru.smn.poker.service;

import ru.smn.poker.action.Action;
import ru.smn.poker.dto.Player;
import ru.smn.poker.game.RoundSettings;

public interface ActionService {

    void setAction(String playerName, Action action);

    void waitUntilPlayerWillHasAction(Player player, RoundSettings roundSettings);

    void changeStateType(String playerName);

}
