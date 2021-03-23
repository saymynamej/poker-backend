package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

public class BetStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, CountAction countAction, TableSettings tableSettings) {
        if (canNotDoBet(tableSettings, countAction)) {
            actionService.waitPlayerAction(player, tableSettings);
        }
        gameService.doAction(player, tableSettings, countAction.getCount(), countAction.getCount(), countAction);
    }

    public boolean canNotDoBet(TableSettings tableSettings, CountAction countAction){
        return tableSettings.getLastBet() != 0 && countAction.getCount() != 0 && countAction.getCount() >= tableSettings.getBigBlindBet();
    }
}
