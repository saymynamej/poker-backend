package ru.sm.poker.action.strategy;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.util.HistoryUtil.allActionsWithNewActionEqualsLastBet;
import static ru.sm.poker.util.HistoryUtil.sumAllHistoryBetsWithoutBlinds;

public class CallStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        if (roundSettingsDTO.getStageType() != StageType.PREFLOP) {
            final long bets = sumAllHistoryBetsWithoutBlinds(roundSettingsDTO, player, countAction);
            if (bets == roundSettingsDTO.getLastBet()) {
                gameService.removeChipsFromPlayer(player, roundSettingsDTO, countAction.getCount(), bets);
                return;
            }
            actionService.waitUntilPlayerWillHasAction(player, roundSettingsDTO);
        }

        final long sumBeforeBets = allActionsWithNewActionEqualsLastBet(roundSettingsDTO, player, countAction);
        if (sumBeforeBets == roundSettingsDTO.getLastBet()) {
            gameService.removeChipsFromPlayer(player, roundSettingsDTO, countAction.getCount(), sumBeforeBets);
            return;
        }
        actionService.waitUntilPlayerWillHasAction(player, roundSettingsDTO);
    }
}
