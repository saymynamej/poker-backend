package ru.sm.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sm.poker.action.Action;
import ru.sm.poker.enums.ActionType;

@RequiredArgsConstructor
@Getter
public class Fold implements Action {


    @Override
    public ActionType getActionType() {
        return ActionType.FOLD;
    }
}
