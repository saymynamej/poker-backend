package ru.sm.poker.action.holdem.strategy;

import ru.sm.poker.action.ActionStrategy;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

public class CheckStrategy implements ActionStrategy {

    @Override
    public void execute(PlayerDTO playerDTO, GameService gameService, ActionService actionService, CountAction countAction, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        if (holdemRoundSettingsDTO.getStageType() == StageType.PREFLOP && playerDTO.isBigBlind() && holdemRoundSettingsDTO.getBigBlindBet() == holdemRoundSettingsDTO.getLastBet()) {
            return;
        }
        if (holdemRoundSettingsDTO.getStageType() != StageType.PREFLOP && holdemRoundSettingsDTO.getLastBet() == 0) {
            return;
        }
        actionService.waitUntilPlayerWillHasAction(playerDTO, holdemRoundSettingsDTO);

    }
}
