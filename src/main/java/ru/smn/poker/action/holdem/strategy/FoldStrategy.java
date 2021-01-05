package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;

public class FoldStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettings roundSettings) {
        gameService.log(player, roundSettings, countAction);
    }
}
