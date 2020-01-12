package ru.sm.poker.action.strategy.call;

import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.service.ActionService;

public class CallBigBlindStrategy implements ActionStrategy {

    private final ActionStrategy actionStrategy = new CallCommonStrategy();

    @Override
    public void execute(Player player, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        if (!player.isBigBlind()){
            throw new RuntimeException("player:" + player.getName() + ", must be big blind");
        }

        if (roundSettingsDTO.getStageType() == StageType.PREFLOP) {
            long bet = countAction.getCount() + roundSettingsDTO.getBigBlindBet();
            if (bet == roundSettingsDTO.getLastBet()) {
                actionService.removeChipsPlayerAndAddToBank(player, countAction.getCount(), roundSettingsDTO);
                actionService.setLastBet(roundSettingsDTO, bet);
            } else {
                actionService.waitOneMoreAction(player, roundSettingsDTO);
            }
        } else {
            actionStrategy.execute(player, actionService, countAction, roundSettingsDTO);
        }
    }
}
