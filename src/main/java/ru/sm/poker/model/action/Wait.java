package ru.sm.poker.model.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sm.poker.enums.ActionType;

@Getter
@RequiredArgsConstructor
public class Wait implements Action {

    private final String gameName;

    @Override
    public ActionType getActionType() {
        return ActionType.WAIT;
    }
}
