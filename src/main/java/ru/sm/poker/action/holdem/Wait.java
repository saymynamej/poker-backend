package ru.sm.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.action.Action;

@Getter
@RequiredArgsConstructor
public class Wait implements Action {

    @Override
    public ActionType getActionType() {
        return ActionType.WAIT;
    }

}
