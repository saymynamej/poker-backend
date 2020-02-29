package ru.sm.poker.action.holdem.strategy;

import ru.sm.poker.action.ActionStrategy;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

public class CheckStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettings holdemRoundSettings) {
        if (isPreflopAndBigBlindCanCheck(holdemRoundSettings, player)) {
            return;
        }
        if (isPostFlopAndLastBetIsZero(holdemRoundSettings)) {
            return;
        }
        actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettings);
    }

    private boolean isPreflopAndBigBlindCanCheck(HoldemRoundSettings holdemRoundSettings, Player player){
        return holdemRoundSettings.getStageType() == StageType.PREFLOP && player.isBigBlind() && holdemRoundSettings.getBigBlindBet() == holdemRoundSettings.getLastBet();
    }

    private boolean isPostFlopAndLastBetIsZero(HoldemRoundSettings holdemRoundSettings){
        return holdemRoundSettings.getStageType() != StageType.PREFLOP && holdemRoundSettings.getLastBet() == 0;
    }

}
