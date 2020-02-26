package ru.sm.poker.action.holdem.strategy;

import ru.sm.poker.action.ActionStrategy;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.holdem.Raise;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.util.HistoryUtil.sumStageHistoryBets;
import static ru.sm.poker.util.PlayerUtil.hasPlayerEnoughChips;

public class RaiseStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {

        if (!hasPlayerEnoughChips(player, countAction)) {
            actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettingsDTO);
            return;
        }

        if (countAction.getCount() < holdemRoundSettingsDTO.getLastBet() * 2) {
            actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettingsDTO);
            return;
        }

        final long prevBets = sumStageHistoryBets(holdemRoundSettingsDTO, player);

        //CHANGE ACTION FOR HISTORY
        player.setAction(new Raise(countAction.getCount() - prevBets));

        gameService.doAction(player, holdemRoundSettingsDTO, countAction.getCount() - prevBets, countAction.getCount());

    }
}
