package ru.sm.poker.action.holdem.strategy;

import ru.sm.poker.action.ActionStrategy;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.holdem.Raise;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.util.HistoryUtil.sumAllHistoryBets;
import static ru.sm.poker.util.PlayerUtil.checkPlayerHasEnoughChips;

public class RaiseStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {

        if (!checkPlayerHasEnoughChips(player, countAction)) {
            actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettingsDTO);
            return;
        }

        if (countAction.getCount() < holdemRoundSettingsDTO.getLastBet() * 2) {
            actionService.waitUntilPlayerWillHasAction(player, holdemRoundSettingsDTO);
            return;
        }

        final long prevBets = sumAllHistoryBets(holdemRoundSettingsDTO, player);
        if (holdemRoundSettingsDTO.getStageType() == StageType.PREFLOP && player.isBigBlind() || player.isSmallBlind()) {
            player.setAction(new Raise(countAction.getCount() - prevBets));
        }

        gameService.removeChipsFromPlayer(player, holdemRoundSettingsDTO, countAction.getCount() - prevBets, countAction.getCount());

    }
}
