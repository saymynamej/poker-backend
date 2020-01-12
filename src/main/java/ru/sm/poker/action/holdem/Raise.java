package ru.sm.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.sm.poker.action.strategy.raise.CommonRaiseStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.ExecutableAction;
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
        new CommonRaiseStrategy().execute(player, actionService, this, roundSettingsDTO);
    }
}
