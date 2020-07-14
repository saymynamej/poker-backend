package ru.smn.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.smn.poker.action.Action;
import ru.smn.poker.enums.ActionType;

@RequiredArgsConstructor
@Getter
@ToString
public class Fold implements Action {

    @Override
    public ActionType getActionType() {
        return ActionType.FOLD;
    }
}
