package ru.sm.poker.action.strategy.check;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

public class CommonCheckStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        if (!(player.isBigBlind() && roundSettingsDTO.getBigBlindBet() == roundSettingsDTO.getLastBet())) {
            actionService.waitOneMoreAction(player, roundSettingsDTO);
        }
    }
}
