package ru.sm.poker.model.action;

import ru.sm.poker.enums.ActionType;


public class Bet implements Action {

    private final long count;



    public Bet(long count) {
        this.count = count;
    }

    @Override
    public ActionType getActionType() {
        return ActionType.BET;
    }

    public long getCount() {
        return count;
    }
}
