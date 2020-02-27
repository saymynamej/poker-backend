package ru.sm.poker.action.holdem.strategy;

import ru.sm.poker.action.ActionStrategy;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.util.HistoryUtil.sumAllHistoryBetsWithNewAction;

public class CallStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettings holdemRoundSettings) {
        if (player.hasNotChipsForAction(countAction)) {
            actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettings);
            return;
        }

        final long bets = sumAllHistoryBetsWithNewAction(holdemRoundSettings, player, countAction);

        if (bets == holdemRoundSettings.getLastBet()) {
            gameService.doAction(player, holdemRoundSettings, countAction.getCount(), bets);
            return;
        }
        actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettings);
    }
}
