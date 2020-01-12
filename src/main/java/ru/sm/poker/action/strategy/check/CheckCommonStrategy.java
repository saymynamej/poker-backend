package ru.sm.poker.action.strategy.check;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

public class CheckCommonStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        if (roundSettingsDTO.getStageType() == StageType.PREFLOP) {
            if (!(player.isBigBlind() && roundSettingsDTO.getBigBlindBet() == roundSettingsDTO.getLastBet())) {
                actionService.waitOneMoreAction(player, roundSettingsDTO);
            }
        } else {
            if (roundSettingsDTO.getLastBet() != 0) {
                actionService.waitOneMoreAction(player, roundSettingsDTO);
            }
        }
    }
}
