package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;

public class CheckStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettings roundSettings) {
        if (isPreflopAndBigBlindCanCheck(roundSettings, player)) {
            return;
        }
        if (isPostFlopAndLastBetIsZero(roundSettings)) {
            return;
        }
        actionService.waitUntilPlayerWillHasAction(player, roundSettings);
    }

    private boolean isPreflopAndBigBlindCanCheck(RoundSettings roundSettings, PlayerEntity player){
        return roundSettings.getStageType() == StageType.PREFLOP && player.isBigBlind() && roundSettings.getBigBlindBet() == roundSettings.getLastBet();
    }

    private boolean isPostFlopAndLastBetIsZero(RoundSettings roundSettings){
        return roundSettings.getStageType() != StageType.PREFLOP && roundSettings.getLastBet() == 0;
    }

}
