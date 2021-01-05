package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;

import static ru.smn.poker.util.HistoryUtil.sumAllHistoryBetsWithNewAction;

public class AllStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettings roundSettings) {
        if (countActionNotEqualsChipsCount(countAction, player)) {
            actionService.waitPlayerAction(player, roundSettings);
            return;
        }

        final long allBets = sumAllHistoryBetsWithNewAction(roundSettings, player, countAction);
        if (allBetsMoreThanLastBet(allBets, roundSettings.getLastBet())) {
            gameService.doAction(player, roundSettings, countAction.getCount(), allBets);
            return;
        }

        gameService.doAction(player, roundSettings, countAction.getCount(), roundSettings.getLastBet());
        gameService.log(player, roundSettings, countAction);
    }

    private boolean allBetsMoreThanLastBet(long allBets, long lastBet) {
        return allBets >= lastBet;
    }

    private boolean countActionNotEqualsChipsCount(CountAction countAction, PlayerEntity player) {
        return countAction.getCount() != player.getChipsCount().getCount();
    }
}
