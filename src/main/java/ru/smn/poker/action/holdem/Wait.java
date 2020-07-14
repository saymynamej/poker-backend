package ru.smn.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.smn.poker.action.Action;
import ru.smn.poker.enums.ActionType;

@Getter
@RequiredArgsConstructor
@ToString
public class Wait implements Action {

    @Override
    public ActionType getActionType() {
        return ActionType.WAIT;
    }

}
