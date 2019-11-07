package ru.sm.poker.model.action;

import ru.sm.poker.enums.ActionType;

public class Wait implements Action {
    @Override
    public ActionType getActionType() {
        return ActionType.WAIT;
    }
}
