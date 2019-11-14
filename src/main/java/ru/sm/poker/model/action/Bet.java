package ru.sm.poker.model.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.sm.poker.enums.ActionType;

@Getter
@Setter
@RequiredArgsConstructor
public class Bet implements Action {

    private final long count;
    private final String gameName;

    @Override
    public ActionType getActionType() {
        return ActionType.BET;
    }

}
