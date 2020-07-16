package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.action.holdem.Raise;
import ru.smn.poker.dto.Player;
import ru.smn.poker.dto.RoundSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

import static ru.smn.poker.util.HistoryUtil.sumStageHistoryBets;

public class RaiseStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettings roundSettings) {
        if (player.hasNotChipsForAction(countAction)) {
            actionService.waitUntilPlayerWillHasAction(player, roundSettings);
            return;
        }

        if (raiseLessThanPrevBetByFormulas(roundSettings, countAction)) {
            actionService.waitUntilPlayerWillHasAction(player, roundSettings);
            return;
        }

        final long prevBets = sumStageHistoryBets(roundSettings, player);
        changeActionForHistory(player, countAction, prevBets);
        gameService.doAction(player, roundSettings, countAction.getCount() - prevBets, countAction.getCount());

    }

    private void changeActionForHistory(Player player, CountAction countAction, long prevBets) {
        player.setAction(new Raise(countAction.getCount() - prevBets));
    }
    private boolean raiseLessThanPrevBetByFormulas(RoundSettings roundSettings, CountAction countAction){
        return countAction.getCount() < roundSettings.getLastBet() * 2;
    }

}
