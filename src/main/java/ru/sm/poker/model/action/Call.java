package ru.sm.poker.model.action;

import lombok.*;
import ru.sm.poker.enums.ActionType;

@Getter
@Setter
@RequiredArgsConstructor
public class Call implements Action, CountAction {
    private final long count;
    private final String gameName;

    @Override
    public ActionType getActionType() {
        return ActionType.CALL;
    }
}
