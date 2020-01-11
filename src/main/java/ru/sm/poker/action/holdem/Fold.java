package ru.sm.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.action.Action;

@RequiredArgsConstructor
@Getter
public class Fold implements Action {

    private final String gameName;

    @Override
    public ActionType getActionType() {
        return ActionType.FOLD;
    }
}
