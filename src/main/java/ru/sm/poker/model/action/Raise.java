package ru.sm.poker.model.action;

import lombok.*;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;

@Getter
@Setter
@RequiredArgsConstructor
public class Raise implements Action, CountAction {
    private final long count;
    private final String gameName;

    @Override
    public ActionType getActionType() {
        return ActionType.RAISE;
    }

    @Override
    public void doAction(Player player) {
        player.setAction(this);
    }
}
