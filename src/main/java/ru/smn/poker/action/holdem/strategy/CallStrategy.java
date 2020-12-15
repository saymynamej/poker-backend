package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;
import ru.smn.poker.util.HistoryUtil;

public class CallStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettings roundSettings) {
        if (player.hasNotChipsForAction(countAction)) {
            actionService.waitUntilPlayerWillHasAction(player, roundSettings);
            return;
        }

        final long bets = HistoryUtil.sumAllHistoryBetsWithNewAction(roundSettings, player, countAction);

        if (allBetsNotEqualsLastBet(bets, roundSettings.getLastBet())) {
            actionService.waitUntilPlayerWillHasAction(player, roundSettings);
            return;
        }

        gameService.doAction(player, roundSettings, countAction.getCount(), bets);
    }

    private boolean allBetsNotEqualsLastBet(long allBets, long lastBet){
        return allBets != lastBet;
    }
}
