package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.action.holdem.Raise;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;

import static ru.smn.poker.util.HistoryUtil.sumStageHistoryBets;

public class RaiseStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, CountAction countAction, TableSettings tableSettings) {
        if (player.hasNotChipsForAction(countAction)) {
            actionService.waitPlayerAction(player, tableSettings);
            return;
        }

        if (raiseLessThanPrevBetByFormulas(tableSettings, countAction)) {
            actionService.waitPlayerAction(player, tableSettings);
            return;
        }
        final long prevBets = sumStageHistoryBets(tableSettings, player);
        changeActionForHistory(player, countAction, prevBets);
        gameService.doAction(player, tableSettings, countAction.getCount() - prevBets, countAction.getCount(), countAction);
    }

    private void changeActionForHistory(PlayerEntity player, CountAction countAction, long prevBets) {
        player.setAction(new Raise(countAction.getCount() - prevBets));
    }

    private boolean raiseLessThanPrevBetByFormulas(TableSettings tableSettings, CountAction countAction) {
        return countAction.getCount() < tableSettings.getLastBet() * 2;
    }

}
