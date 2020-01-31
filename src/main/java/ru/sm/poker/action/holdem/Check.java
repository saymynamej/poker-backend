package ru.sm.poker.action.holdem;

import lombok.Getter;
import lombok.ToString;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.base.StrategyBase.CHECK_STRATEGIES;

@ToString
public class Check implements ExecutableAction {

    @Getter
    private final long count = 0;


    @Override
    public ActionType getActionType() {
        return ActionType.CHECK;
    }

    @Override
    public void doAction(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO, GameService gameService, ActionService actionService) {
        CHECK_STRATEGIES.get(playerDTO.getRoleType()).execute(
                playerDTO,
                gameService,
                actionService,
                this,
                holdemRoundSettingsDTO
        );
    }
}
