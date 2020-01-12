package ru.sm.poker.action.strategy.call;

import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

@Slf4j
public class CallCommonStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        if (countAction.getCount() != roundSettingsDTO.getLastBet()) {
            log.info("wait one more action by player: " + player.getName() + " because lastBet: " + roundSettingsDTO.getLastBet() + " but his bet: " + countAction.getCount());
            actionService.waitOneMoreAction(player, roundSettingsDTO);
            return;
        }
        actionService.removeChipsPlayerAndAddToBank(player, countAction.getCount(), roundSettingsDTO);
        actionService.setLastBet(roundSettingsDTO, countAction.getCount());
    }
}
