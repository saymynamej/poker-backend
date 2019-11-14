package ru.sm.poker.game;

import ru.sm.poker.model.action.Action;

public interface ActionHandler {
    void setAction(String playerName, Action action);
}
