package ru.sm.poker.model.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;

@Getter
@RequiredArgsConstructor
public class Wait implements Action {

    private final String gameName;

    @Override
    public ActionType getActionType() {
        return ActionType.WAIT;
    }

    @Override
    public void doAction(Player player) {
        player.setAction(this);
    }
}
