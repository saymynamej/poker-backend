package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

public class CheckStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, CountAction countAction, TableSettings tableSettings) {
        if (isPreflopAndBigBlindCanCheck(tableSettings, player) || isPostFlopAndLastBetIsZero(tableSettings)) {
            gameService.doAction(player,tableSettings,0,tableSettings.getLastBet(), countAction);
            return;
        }
        actionService.waitPlayerAction(player, tableSettings);
    }

    private boolean isPreflopAndBigBlindCanCheck(TableSettings tableSettings, PlayerEntity player){
        return tableSettings.getStageType() == StageType.PREFLOP && player.isBigBlind() && tableSettings.getBigBlindBet() == tableSettings.getLastBet();
    }

    private boolean isPostFlopAndLastBetIsZero(TableSettings tableSettings){
        return tableSettings.getStageType() != StageType.PREFLOP && tableSettings.getLastBet() == 0;
    }

}
