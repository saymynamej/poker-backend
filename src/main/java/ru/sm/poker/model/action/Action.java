package ru.sm.poker.model.action;

import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;

public interface  Action {
    String getGameName();
    ActionType getActionType();
    void doAction(Player player);
}
