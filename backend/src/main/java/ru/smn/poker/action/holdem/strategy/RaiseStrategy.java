package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.Action;
import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.holdem.Raise;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

import static ru.smn.poker.util.HistoryUtil.sumStageHistoryBets;

public class RaiseStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, Action action, TableSettings tableSettings) {
        if (player.getTableSettings().hasNotChipsForAction(action)) {
            actionService.waitPlayerAction(player, tableSettings);
            return;
        }

        if (raiseLessThanPrevBetByFormulas(tableSettings, action)) {
            actionService.waitPlayerAction(player, tableSettings);
            return;
        }
        final long prevBets = sumStageHistoryBets(tableSettings, player);
        changeActionForHistory(player, action, prevBets);
        gameService.doAction(player, tableSettings, action.getCount() - prevBets, action.getCount(), action);
    }

    private void changeActionForHistory(PlayerEntity player, Action action, long prevBets) {
        player.getTableSettings().setAction(new Raise(action.getCount() - prevBets));
    }

    private boolean raiseLessThanPrevBetByFormulas(TableSettings tableSettings, Action action) {
        return action.getCount() < tableSettings.getLastBet() * 2;
    }

}
