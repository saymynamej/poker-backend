package ru.sm.poker.action.strategy;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

public class RaiseStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        if (countAction.getCount() < roundSettingsDTO.getLastBet() * 2) {
            actionService.waitUntilPlayerWillHasAction(player, roundSettingsDTO);
            return;
        }

        if (roundSettingsDTO.getStageType() == StageType.PREFLOP) {
            if (player.isSmallBlind()) {
                gameService.removeChipsFromPlayer(player, roundSettingsDTO, countAction.getCount() - roundSettingsDTO.getSmallBlindBet(), countAction.getCount());
            } else if (player.isBigBlind()) {
                gameService.removeChipsFromPlayer(player, roundSettingsDTO, countAction.getCount() - roundSettingsDTO.getBigBlindBet(), countAction.getCount());
            } else {
                gameService.removeChipsFromPlayer(player, roundSettingsDTO, countAction.getCount(), countAction.getCount());
            }
            return;
        }
        gameService.removeChipsFromPlayer(player, roundSettingsDTO, countAction.getCount(), countAction.getCount());
    }
}
