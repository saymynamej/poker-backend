package ru.sm.poker.action.strategy;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.holdem.Raise;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.util.HistoryUtil.*;

public class RaiseStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        final long prevBets = sumAllHistoryBets(roundSettingsDTO, player);
        if (roundSettingsDTO.getStageType() == StageType.PREFLOP) {
            switch (player.getRoleType()) {
                case SMALL_BLIND:
                case BIG_BLIND:
                    player.setAction(new Raise(countAction.getCount() - prevBets));
                    gameService.removeChipsFromPlayer(player, roundSettingsDTO, countAction.getCount() - prevBets, countAction.getCount());
                    break;
                default:
                    gameService.removeChipsFromPlayer(player, roundSettingsDTO, countAction.getCount() - prevBets, countAction.getCount());
                    break;
            }
            return;
        }

        if (countAction.getCount() > roundSettingsDTO.getLastBet() * 2) {
            gameService.removeChipsFromPlayer(player, roundSettingsDTO, countAction.getCount() - prevBets, countAction.getCount());
            return;
        }
        actionService.waitUntilPlayerWillHasAction(player, roundSettingsDTO);
    }
}
