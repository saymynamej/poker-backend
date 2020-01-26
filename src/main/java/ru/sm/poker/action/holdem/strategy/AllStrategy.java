package ru.sm.poker.action.holdem.strategy;

import ru.sm.poker.action.ActionStrategy;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.util.HistoryUtil.*;

public class AllStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        if (countAction.getCount() != player.getChipsCount()) {
            actionService.waitUntilPlayerWillHasAction(player, roundSettingsDTO);
            return;
        }
        final long allBets = sumAllHistoryBetsWithNewAction(roundSettingsDTO, player, countAction);

        if (allBets >= roundSettingsDTO.getLastBet()) {
            gameService.removeChipsFromPlayer(player, roundSettingsDTO, countAction.getCount(), allBets);
            return;
        }
        gameService.removeChipsFromPlayer(player, roundSettingsDTO, countAction.getCount()              , roundSettingsDTO.getLastBet());

    }
}
