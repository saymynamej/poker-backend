package ru.sm.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

@Getter
@Setter
@RequiredArgsConstructor
public class Bet implements ExecutableAction {

    private final long count;

    @Override
    public ActionType getActionType() {
        return ActionType.BET;
    }

    @Override
    public void doAction(RoundSettingsDTO roundSettingsDTOm, Player player, ActionService actionService) {

    }
}
