package ru.sm.poker.action.strategy.raise;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

public class RaiseCommonStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        if (countAction.getCount() < roundSettingsDTO.getLastBet() * 2) {
            actionService.waitOneMoreAction(player, roundSettingsDTO);
        }
        actionService.removeChipsPlayerAndAddToBank(player, countAction.getCount(), roundSettingsDTO);
        actionService.setLastBet(roundSettingsDTO, countAction.getCount());
    }
}
