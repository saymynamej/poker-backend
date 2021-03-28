package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.Action;
import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

public class BetStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, Action action, TableSettings tableSettings) {
        if (canNotDoBet(tableSettings, action)) {
            actionService.waitPlayerAction(player, tableSettings);
        }
        gameService.doAction(player, tableSettings, action.getCount(), action.getCount(), action);
    }

    public boolean canNotDoBet(TableSettings tableSettings, Action action){
        return tableSettings.getLastBet() != 0 && action.getCount() != 0 && action.getCount() >= tableSettings.getBigBlindBet();
    }
}
