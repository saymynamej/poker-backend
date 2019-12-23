package ru.sm.poker.service;

import ru.sm.poker.model.Player;
import ru.sm.poker.model.RoundSettings;
import ru.sm.poker.model.action.Action;

public interface ActionService {
    void setAction(String playerName, Action action);
    void parseAction(Player player, RoundSettings roundSettings);
    void setActions(RoundSettings roundSettings);
}
