package ru.sm.poker.action.holdem;

import lombok.RequiredArgsConstructor;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.base.StrategyBase.*;

@RequiredArgsConstructor
public class All implements ExecutableAction {

    private final long count;

    @Override
    public ActionType getActionType() {
        return ActionType.ALLIN;
    }

    @Override
    public void doAction(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO, GameService gameService, ActionService actionService) {
        ALLIN_STRATEGIES.get(playerDTO.getRoleType()).execute(
                playerDTO,
                gameService,
                actionService,
                this,
                holdemRoundSettingsDTO
        );
    }

    @Override
    public long getCount() {
        return count;
    }
}
