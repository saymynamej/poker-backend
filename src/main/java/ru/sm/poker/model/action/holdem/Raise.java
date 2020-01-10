package ru.sm.poker.model.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.CountAction;
import ru.sm.poker.model.action.ExecutableAction;
import ru.sm.poker.service.ActionService;

@Getter
@Setter
@RequiredArgsConstructor
public class Raise implements ExecutableAction, CountAction {
    private final long count;
    private final String gameName;

    @Override
    public ActionType getActionType() {
        return ActionType.RAISE;
    }

    @Override
    public void doAction(RoundSettingsDTO roundSettingsDTO, Player player, ActionService actionService) {
        if (getCount() < roundSettingsDTO.getLastBet() * 2) {
            actionService.setPlayerWait(player);
            actionService.waitPlayerAction(player, roundSettingsDTO);
        }
        actionService.removeChipsPlayerAndAddToBank(player, getCount(), roundSettingsDTO);
        actionService.setLastBet(roundSettingsDTO, getCount());
    }
}
