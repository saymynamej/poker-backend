package ru.sm.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.sm.poker.action.Action;
import ru.sm.poker.enums.ActionType;

@Getter
@RequiredArgsConstructor
@ToString
public class Wait implements Action {

    @Override
    public ActionType getActionType() {
        return ActionType.WAIT;
    }

}
