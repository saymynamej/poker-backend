package ru.sm.poker.model.action;

import ru.sm.poker.enums.ActionType;

public class Raise implements Action {



    @Override
    public ActionType getActionType() {
        return ActionType.RAISE;
    }
}
