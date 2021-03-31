package ru.smn.poker.enums;

import ru.smn.poker.action.Action;
import ru.smn.poker.action.holdem.*;
import ru.smn.poker.dto.ActionInfo;

public enum ActionType {
    BET,
    FOLD,
    RAISE,
    CALL,
    WAIT,
    CHECK,
    ALLIN,
    SB_BET,
    BB_BET,
    BUTTON_BET;

    public static Action getActionByType(ActionType actionType, Long count) {
        switch (actionType) {
            case BET:
            case BB_BET:
            case BUTTON_BET:
            case SB_BET:
                return new Bet(count);
            case CALL:
                return new Call(count);
            case FOLD:
                return new Fold();
            case ALLIN:
                return new AllIn(count);
            case WAIT:
                return new Wait();
            case CHECK:
                return new Check();
            case RAISE:
                return new Raise(count);
        }
        throw new RuntimeException("global error with defining action type");
    }

}
