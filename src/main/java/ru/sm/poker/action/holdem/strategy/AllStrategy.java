package ru.sm.poker.action.holdem.strategy;

import ru.sm.poker.action.ActionStrategy;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.util.HistoryUtil.*;

public class AllStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        if (countAction.getCount() != player.getChipsCount()) {
            actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettingsDTO);
            return;
        }
        final long allBets = sumAllHistoryBetsWithNewAction(holdemRoundSettingsDTO, player, countAction);

        if (allBets >= holdemRoundSettingsDTO.getLastBet()) {
            gameService.removeChipsFromPlayer(player, holdemRoundSettingsDTO, countAction.getCount(), allBets);
            return;
        }
        gameService.removeChipsFromPlayer(player, holdemRoundSettingsDTO, countAction.getCount(), holdemRoundSettingsDTO.getLastBet());

    }
}
