package ru.sm.poker.action.holdem.strategy;

import ru.sm.poker.action.ActionStrategy;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

public class BetStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettings holdemRoundSettings) {
        if (holdemRoundSettings.getLastBet() != 0 && countAction.getCount() != 0 && countAction.getCount() >= holdemRoundSettings.getBigBlindBet()) {
            actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettings);
        }
        gameService.doAction(player,holdemRoundSettings, countAction.getCount(), countAction.getCount());
    }
}
