package ru.sm.poker.action.holdem.strategy;

import ru.sm.poker.action.ActionStrategy;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.holdem.Raise;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.util.HistoryUtil.sumStageHistoryBets;
import static ru.sm.poker.util.PlayerUtil.checkPlayerHasEnoughChips;

public class RaiseStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerDTO playerDTO, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {

        if (!checkPlayerHasEnoughChips(playerDTO, countAction)) {
            actionService.waitUntilPlayerWillHasAction(playerDTO, holdemRoundSettingsDTO);
            return;
        }

        if (countAction.getCount() < holdemRoundSettingsDTO.getLastBet() * 2) {
            actionService.waitUntilPlayerWillHasAction(playerDTO, holdemRoundSettingsDTO);
            return;
        }

        final long prevBets = sumStageHistoryBets(holdemRoundSettingsDTO, playerDTO);

        //CHANGE ACTION FOR HISTORY
        playerDTO.setAction(new Raise(countAction.getCount() - prevBets));

        gameService.doAction(playerDTO, holdemRoundSettingsDTO, countAction.getCount() - prevBets, countAction.getCount());

    }
}
