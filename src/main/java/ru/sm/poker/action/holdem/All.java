package ru.sm.poker.action.holdem;

import lombok.RequiredArgsConstructor;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.base.StrategyBase.ALLIN_STRATEGIES;

@RequiredArgsConstructor
public class All implements ExecutableAction {

    private final long count;

    @Override
    public ActionType getActionType() {
        return ActionType.ALLIN;
    }

    @Override
    public void doAction(HoldemRoundSettingsDTO holdemRoundSettingsDTO, Player player, GameService gameService, ActionService actionService) {
        ALLIN_STRATEGIES.get(player.getRoleType()).execute(
                player,
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
