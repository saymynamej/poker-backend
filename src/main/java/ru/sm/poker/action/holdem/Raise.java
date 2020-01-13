package ru.sm.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.base.StrategyBase;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

@Getter
@Setter
@RequiredArgsConstructor
public class Raise implements ExecutableAction, CountAction {

    private final long count;

    @Override
    public ActionType getActionType() {
        return ActionType.RAISE;
    }

    @Override
    public void doAction(RoundSettingsDTO roundSettingsDTO, Player player, ActionService actionService) {
        StrategyBase.RAISE_STRATEGIES.get(player.getRoleType()).execute(player, actionService, this, roundSettingsDTO);
    }
}
