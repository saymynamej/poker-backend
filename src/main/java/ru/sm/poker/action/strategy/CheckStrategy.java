package ru.sm.poker.action.strategy;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

public class CheckStrategy implements ActionStrategy {

    @Override
    public void execute(Player player, GameService gameService, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO) {
        if (roundSettingsDTO.getStageType() == StageType.PREFLOP && player.isBigBlind() && roundSettingsDTO.getBigBlindBet() == roundSettingsDTO.getLastBet()) {
            return;
        }
        if (roundSettingsDTO.getStageType() != StageType.PREFLOP && roundSettingsDTO.getLastBet() == 0) {
            return;
        }
        actionService.waitUntilPlayerWillHasAction(player, roundSettingsDTO);

    }
}