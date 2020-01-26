package ru.sm.poker.action.holdem.strategy;

import ru.sm.poker.action.ActionStrategy;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

public class BetStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        if (holdemRoundSettingsDTO.getLastBet() != 0 && countAction.getCount() != 0 && countAction.getCount() >= holdemRoundSettingsDTO.getBigBlindBet()) {
            actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettingsDTO);
        }
    }
}
