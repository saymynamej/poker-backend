package ru.sm.poker.action.holdem.strategy;

import ru.sm.poker.action.ActionStrategy;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.util.HistoryUtil.sumAllHistoryBetsWithNewAction;
import static ru.sm.poker.util.PlayerUtil.checkPlayerHasEnoughChips;

public class CallStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        if (!checkPlayerHasEnoughChips(player, countAction)) {
            actionService.waitUntilPlayerWillHasAction(player, roundSettingsDTO);
            return;
        }

        final long bets = sumAllHistoryBetsWithNewAction(roundSettingsDTO, player, countAction);

        if (bets == roundSettingsDTO.getLastBet()) {
            gameService.removeChipsFromPlayer(player, roundSettingsDTO, countAction.getCount(), bets);
            return;
        }
        actionService.waitUntilPlayerWillHasAction(player, roundSettingsDTO);
    }
}
