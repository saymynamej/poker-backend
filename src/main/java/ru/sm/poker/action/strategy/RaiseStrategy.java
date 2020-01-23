package ru.sm.poker.action.strategy;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.holdem.Raise;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.util.HistoryUtil.sumAllHistoryBets;
import static ru.sm.poker.util.PlayerUtil.checkPlayerHasEnoughChips;

public class RaiseStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {

        if (!checkPlayerHasEnoughChips(player, countAction)) {
            actionService.waitUntilPlayerWillHasAction(player, roundSettingsDTO);
            return;
        }

        if (countAction.getCount() < roundSettingsDTO.getLastBet() * 2) {
            actionService.waitUntilPlayerWillHasAction(player, roundSettingsDTO);
            return;
        }

        final long prevBets = sumAllHistoryBets(roundSettingsDTO, player);
        if (roundSettingsDTO.getStageType() == StageType.PREFLOP && player.isBigBlind() || player.isSmallBlind()) {
            player.setAction(new Raise(countAction.getCount() - prevBets));
        }

        gameService.removeChipsFromPlayer(player, roundSettingsDTO, countAction.getCount() - prevBets, countAction.getCount());

    }
}
