package ru.sm.poker.action.holdem.strategy;

import ru.sm.poker.action.ActionStrategy;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.util.HistoryUtil.sumAllHistoryBetsWithNewAction;
import static ru.sm.poker.util.PlayerUtil.checkPlayerHasEnoughChips;

public class CallStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerDTO playerDTO, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        if (!checkPlayerHasEnoughChips(playerDTO, countAction)) {
            actionService.waitUntilPlayerWillHasAction(playerDTO, holdemRoundSettingsDTO);
            return;
        }

        final long bets = sumAllHistoryBetsWithNewAction(holdemRoundSettingsDTO, playerDTO, countAction);

        if (bets == holdemRoundSettingsDTO.getLastBet()) {
            gameService.doAction(playerDTO, holdemRoundSettingsDTO, countAction.getCount(), bets);
            return;
        }
        actionService.waitUntilPlayerWillHasAction(playerDTO, holdemRoundSettingsDTO);
    }
}
