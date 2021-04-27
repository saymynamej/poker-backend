package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.Action;
import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;
import ru.smn.poker.util.HistoryUtil;

public class CallStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, Action action, TableSettings tableSettings) {
        if (player.getTableSettings().hasNotChipsForAction(action)) {
            actionService.waitPlayerAction(player, tableSettings);
            return;
        }

        final long bets = HistoryUtil.sumAllHistoryBetsWithNewAction(tableSettings, player, action);

        if (allBetsNotEqualsLastBet(bets, tableSettings.getLastBet())) {
            actionService.waitPlayerAction(player, tableSettings);
            return;
        }

        gameService.doAction(player, tableSettings, action.getCount(), bets, action);
    }

    private boolean allBetsNotEqualsLastBet(long allBets, long lastBet){
        return allBets != lastBet;
    }
}
