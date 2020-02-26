package ru.sm.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.base.StrategyBase.CALL_STRATEGIES;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Call implements ExecutableAction {

    private final long count;

    @Override
    public ActionType getActionType() {
        return ActionType.CALL;
    }

    @Override
    public void doAction(HoldemRoundSettingsDTO holdemRoundSettingsDTO, Player player, GameService gameService, ActionService actionService) {
        CALL_STRATEGIES.get(player.getRoleType()).execute
                (
                        player,
                        gameService,
                        actionService,
                        this,
                        holdemRoundSettingsDTO
                );
    }
}
