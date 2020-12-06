package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

public class BetStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettings roundSettings) {
        if (canNotDoBet(roundSettings, countAction)) {
            actionService.waitUntilPlayerWillHasAction(player, roundSettings);
        }
        gameService.doAction(player, roundSettings, countAction.getCount(), countAction.getCount());
    }

    public boolean canNotDoBet(RoundSettings roundSettings, CountAction countAction){
        return roundSettings.getLastBet() != 0 && countAction.getCount() != 0 && countAction.getCount() >= roundSettings.getBigBlindBet();
    }
}
