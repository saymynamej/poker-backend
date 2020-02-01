package ru.sm.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.base.StrategyBase.BET_STRATEGIES;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Bet implements ExecutableAction {

    private final long count;


    @Override
    public ActionType getActionType() {
        return ActionType.BET;
    }

    @Override
    public void doAction(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO, GameService gameService, ActionService actionService) {
        BET_STRATEGIES.get(playerDTO.getRoleType()).execute(
                playerDTO,
                gameService,
                actionService,
                this,
                holdemRoundSettingsDTO
        );
    }
}
