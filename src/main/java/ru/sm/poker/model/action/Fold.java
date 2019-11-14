package ru.sm.poker.model.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sm.poker.enums.ActionType;

@RequiredArgsConstructor
@Getter
public class Fold implements Action {

    private final String gameName;

    @Override
    public ActionType getActionType() {
        return ActionType.FOLD;
    }
}
