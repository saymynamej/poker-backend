package ru.sm.poker.action.strategy.call;

import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.service.ActionService;

public class CallSmallBlindStrategy implements ActionStrategy {

    private final ActionStrategy callCommonStrategy = new CallCommonStrategy();

    @Override
    public void execute(Player player, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        if (!player.isSmallBlind()){
            throw new RuntimeException("player:" + player.getName() + ", must be small blind");
        }

        if (roundSettingsDTO.getStageType() == StageType.PREFLOP) {
            final long bet = countAction.getCount() + countAction.getCount();
            if (bet == roundSettingsDTO.getBigBlindBet() && roundSettingsDTO.getLastBet() == roundSettingsDTO.getBigBlindBet()) {
                actionService.removeChipsPlayerAndAddToBank(player, countAction.getCount(), roundSettingsDTO);
                actionService.setLastBet(roundSettingsDTO, bet);
            } else if (roundSettingsDTO.getLastBet() != bet) {
                if (countAction.getCount() + roundSettingsDTO.getSmallBlindBet() == roundSettingsDTO.getLastBet()) {
                    actionService.removeChipsPlayerAndAddToBank(player, countAction.getCount(), roundSettingsDTO);
                    actionService.setLastBet(roundSettingsDTO, countAction.getCount() + roundSettingsDTO.getSmallBlindBet());
                } else {
                    actionService.waitOneMoreAction(player, roundSettingsDTO);
                }
            } else {
                actionService.waitOneMoreAction(player, roundSettingsDTO);
            }
        } else {
            callCommonStrategy.execute(player, actionService, countAction, roundSettingsDTO);
        }
    }

}
