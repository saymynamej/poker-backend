package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;
import ru.smn.poker.util.HistoryUtil;

public class CallStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerEntity player, GameService gameService, ActionService actionService, CountAction countAction, TableSettings tableSettings) {
        if (player.getTableSettings().hasNotChipsForAction(countAction)) {
            actionService.waitPlayerAction(player, tableSettings);
            return;
        }

        final long bets = HistoryUtil.sumAllHistoryBetsWithNewAction(tableSettings, player, countAction);

        if (allBetsNotEqualsLastBet(bets, tableSettings.getLastBet())) {
            actionService.waitPlayerAction(player, tableSettings);
            return;
        }

        gameService.doAction(player, tableSettings, countAction.getCount(), bets, countAction);
    }

    private boolean allBetsNotEqualsLastBet(long allBets, long lastBet){
        return allBets != lastBet;
    }
}
