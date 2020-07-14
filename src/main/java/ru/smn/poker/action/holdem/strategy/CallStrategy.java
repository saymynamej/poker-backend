package ru.smn.poker.action.holdem.strategy;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;
import ru.smn.poker.util.HistoryUtil;

public class CallStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettings holdemRoundSettings) {
        if (player.hasNotChipsForAction(countAction)) {
            actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettings);
            return;
        }

        final long bets = HistoryUtil.sumAllHistoryBetsWithNewAction(holdemRoundSettings, player, countAction);

        if (allBetsNotEqualsLastBet(bets, holdemRoundSettings.getLastBet())) {
            actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettings);
            return;
        }

        gameService.doAction(player, holdemRoundSettings, countAction.getCount(), bets);
    }

    private boolean allBetsNotEqualsLastBet(long allBets, long lastBet){
        return allBets != lastBet;
    }
}
