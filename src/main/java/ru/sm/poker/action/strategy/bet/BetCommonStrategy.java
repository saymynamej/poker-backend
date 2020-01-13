package ru.sm.poker.action.strategy.bet;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

public class BetCommonStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        if (roundSettingsDTO.getLastBet() != 0) {
            actionService.waitOneMoreAction(player, roundSettingsDTO);
        }
    }
}
