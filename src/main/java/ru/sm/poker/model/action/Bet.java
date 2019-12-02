package ru.sm.poker.model.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;

@Getter
@Setter
@RequiredArgsConstructor
public class Bet implements Action, CountAction {

    private final long count;
    private final String gameName;

    @Override
    public void doAction(Player player) {
        player.setAction(this);
    }

    @Override
    public ActionType getActionType() {
        return ActionType.BET;
    }

}
