package ru.sm.poker.model.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.ExecutableAction;
import ru.sm.poker.service.ActionService;

@Getter
@Setter
@RequiredArgsConstructor
public class Bet implements ExecutableAction {

    private final long count;
    private final String gameName;

    @Override
    public ActionType getActionType() {
        return ActionType.BET;
    }

    @Override
    public void doAction(RoundSettingsDTO roundSettingsDTOm, Player player, ActionService actionService) {

    }
}
