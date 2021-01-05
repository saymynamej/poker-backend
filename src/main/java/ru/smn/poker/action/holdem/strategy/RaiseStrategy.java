package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.action.holdem.Raise;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;

import static ru.smn.poker.util.HistoryUtil.sumStageHistoryBets;

public class RaiseStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettings roundSettings) {
        if (player.hasNotChipsForAction(countAction)) {
            actionService.waitPlayerAction(player, roundSettings);
            return;
        }

        if (raiseLessThanPrevBetByFormulas(roundSettings, countAction)) {
            actionService.waitPlayerAction(player, roundSettings);
            return;
        }

        final long prevBets = sumStageHistoryBets(roundSettings, player);
        changeActionForHistory(player, countAction, prevBets);
        gameService.doAction(player, roundSettings, countAction.getCount() - prevBets, countAction.getCount());
        gameService.log(player, roundSettings, countAction);
    }

    private void changeActionForHistory(PlayerEntity player, CountAction countAction, long prevBets) {
        player.setAction(new Raise(countAction.getCount() - prevBets));
    }

    private boolean raiseLessThanPrevBetByFormulas(RoundSettings roundSettings, CountAction countAction) {
        return countAction.getCount() < roundSettings.getLastBet() * 2;
    }

}
