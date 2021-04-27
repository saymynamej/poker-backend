package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.Action;
import ru.smn.poker.action.ActionStrategy;
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
            Action action,
            TableSettings tableSettings
    ) {
        if (countActionNotEqualsChipsCount(action, player)) {
            actionService.waitPlayerAction(player, tableSettings);
            return;
        }

        final long allBets = sumAllHistoryBetsWithNewAction(tableSettings, player, action);
        if (allBetsMoreThanLastBet(allBets, tableSettings.getLastBet())) {
            gameService.doAction(
                    player,
                    tableSettings,
                    action.getCount(),
                    allBets,
                    action
            );
            return;
        }
        gameService.doAction(player, tableSettings, action.getCount(), tableSettings.getLastBet(), action);
    }

    private boolean allBetsMoreThanLastBet(long allBets, long lastBet) {
        return allBets >= lastBet;
    }

    private boolean countActionNotEqualsChipsCount(Action action, PlayerEntity player) {
        return action.getCount() != player.getTableSettings().getChipsCount().getCount();
    }
}
