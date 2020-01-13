package ru.sm.poker.action.holdem;

import lombok.Getter;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.base.StrategyBase;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

public class Check implements ExecutableAction {

    @Getter
    private final long count = 0;


    @Override
    public ActionType getActionType() {
        return ActionType.CHECK;
    }

    @Override
    public void doAction(RoundSettingsDTO roundSettingsDTO, Player player, ActionService actionService) {
        StrategyBase.CHECK_STRATEGIES.get(player.getRoleType()).execute(player, actionService, this, roundSettingsDTO);
    }
}
