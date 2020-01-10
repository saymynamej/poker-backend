package ru.sm.poker.model.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Action;
import ru.sm.poker.model.action.ExecutableAction;
import ru.sm.poker.service.ActionService;

@Getter
@RequiredArgsConstructor
public class Wait implements Action, ExecutableAction {

    private final String gameName;

    @Override
    public ActionType getActionType() {
        return ActionType.WAIT;
    }

    @Override
    public void doAction(RoundSettingsDTO roundSettingsDTOm, Player player, ActionService actionService) {
        player.setAction(this);
    }
}
