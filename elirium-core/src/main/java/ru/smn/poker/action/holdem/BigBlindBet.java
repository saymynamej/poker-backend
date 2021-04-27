package ru.smn.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.smn.poker.action.Action;
import ru.smn.poker.enums.ActionType;

@RequiredArgsConstructor
@Getter
public class BigBlindBet implements Action {
    private final long count;

    @Override
    public ActionType getActionType() {
        return ActionType.BB_BET;
    }
}
