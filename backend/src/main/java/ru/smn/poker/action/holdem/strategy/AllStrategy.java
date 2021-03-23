package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

import static ru.smn.poker.util.HistoryUtil.sumAllHistoryBetsWithNewAction;

public class AllStrategy implements ActionStrategy {

    @Override
    public void execute(
            PlayerEntity player,
            GameService gameService,
            ActionService actionService,
            CountAction countAction,
            TableSettings tableSettings
    ) {
        if (countActionNotEqualsChipsCount(countAction, player)) {
            actionService.waitPlayerAction(player, tableSettings);
            return;
        }

        final long allBets = sumAllHistoryBetsWithNewAction(tableSettings, player, countAction);
        if (allBetsMoreThanLastBet(allBets, tableSettings.getLastBet())) {
            gameService.doAction(
                    player,
                    tableSettings,
                    countAction.getCount(),
                    allBets,
                    countAction
            );
            return;
        }
        gameService.doAction(player, tableSettings, countAction.getCount(), tableSettings.getLastBet(), countAction);
    }

    private boolean allBetsMoreThanLastBet(long allBets, long lastBet) {
        return allBets >= lastBet;
    }

    private boolean countActionNotEqualsChipsCount(CountAction countAction, PlayerEntity player) {
        return countAction.getCount() != player.getTableSettings().getChipsCount().getCount();
    }
}
