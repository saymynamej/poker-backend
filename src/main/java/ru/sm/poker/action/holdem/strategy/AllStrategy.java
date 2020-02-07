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
    public void execute(PlayerDTO playerDTO, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        if (countAction.getCount() != playerDTO.getChipsCount()) {
            actionService.waitUntilPlayerWillHasAction(playerDTO, holdemRoundSettingsDTO);
            return;
        }
        final long allBets = sumAllHistoryBetsWithNewAction(holdemRoundSettingsDTO, playerDTO, countAction);

        if (allBets >= holdemRoundSettingsDTO.getLastBet()) {
            gameService.doAction(playerDTO, holdemRoundSettingsDTO, countAction.getCount(), allBets);
            return;
        }
        gameService.doAction(playerDTO, holdemRoundSettingsDTO, countAction.getCount(), holdemRoundSettingsDTO.getLastBet());

    }
}
