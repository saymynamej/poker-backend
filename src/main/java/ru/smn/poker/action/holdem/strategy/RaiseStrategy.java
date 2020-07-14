package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.action.holdem.Raise;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

import static ru.smn.poker.util.HistoryUtil.sumStageHistoryBets;

public class RaiseStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettings holdemRoundSettings) {
        if (player.hasNotChipsForAction(countAction)) {
            actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettings);
            return;
        }

        if (raiseLessThanPrevBetByFormulas(holdemRoundSettings, countAction)) {
            actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettings);
            return;
        }

        final long prevBets = sumStageHistoryBets(holdemRoundSettings, player);
        changeActionForHistory(player, countAction, prevBets);
        gameService.doAction(player, holdemRoundSettings, countAction.getCount() - prevBets, countAction.getCount());

    }

    private void changeActionForHistory(Player player, CountAction countAction, long prevBets) {
        player.setAction(new Raise(countAction.getCount() - prevBets));
    }
    private boolean raiseLessThanPrevBetByFormulas(HoldemRoundSettings holdemRoundSettings, CountAction countAction){
        return countAction.getCount() < holdemRoundSettings.getLastBet() * 2;
    }

}
