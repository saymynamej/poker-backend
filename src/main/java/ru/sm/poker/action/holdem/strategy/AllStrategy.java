package ru.sm.poker.action.holdem.strategy;

import ru.sm.poker.action.ActionStrategy;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.util.HistoryUtil.sumAllHistoryBetsWithNewAction;

public class AllStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerDTO player, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettingsDTO holdemRoundSettings) {
        if (countActionNotEqualsChipsCount(countAction, player)) {
            actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettings);
            return;
        }

        final long allBets = sumAllHistoryBetsWithNewAction(holdemRoundSettings, player, countAction);
        if (allBetsMoreThanLastBet(allBets, holdemRoundSettings.getLastBet())) {
            gameService.doAction(player, holdemRoundSettings, countAction.getCount(), allBets);
            return;
        }

        gameService.doAction(player, holdemRoundSettings, countAction.getCount(), holdemRoundSettings.getLastBet());
    }

    private boolean allBetsMoreThanLastBet(long allBets, long lastBet){
        return allBets >= lastBet;
    }

    private boolean countActionNotEqualsChipsCount(CountAction countAction, PlayerDTO player){
        return countAction.getCount() != player.getChipsCount();
    }
}
