package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

public class FoldStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, CountAction countAction, TableSettings tableSettings) {
        gameService.doAction(player, tableSettings, 0, tableSettings.getLastBet(), countAction);
    }
}
