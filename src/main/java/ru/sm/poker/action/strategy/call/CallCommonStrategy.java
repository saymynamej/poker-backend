package ru.sm.poker.action.strategy.call;

import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.service.ActionService;

public class CallCommonStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        if (countAction.getCount() == roundSettingsDTO.getLastBet()) {
            actionService.removeChipsPlayerAndAddToBank(player, countAction.getCount(), roundSettingsDTO);
            actionService.setLastBet(roundSettingsDTO, countAction.getCount());
        } else {
            actionService.waitOneMoreAction(player, roundSettingsDTO);
        }
    }
}
