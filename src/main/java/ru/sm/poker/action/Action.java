package ru.sm.poker.action;

import ru.sm.poker.enums.ActionType;

public interface Action {
    String getGameName();

    ActionType getActionType();
}
