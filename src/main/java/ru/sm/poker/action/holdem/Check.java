package ru.sm.poker.action.holdem;

import lombok.Getter;
import lombok.ToString;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.data.StrategyData.CHECK_STRATEGIES;

@ToString
public class Check implements ExecutableAction {

    @Getter
    private final long count = 0;

    @Override
    public ActionType getActionType() {
        return ActionType.CHECK;
    }

    @Override
    public void doAction(HoldemRoundSettingsDTO holdemRoundSettings, PlayerDTO player, GameService gameService, ActionService actionService) {
        CHECK_STRATEGIES.get(player.getRoleType()).execute(
                player,
                gameService,
                actionService,
                this,
                holdemRoundSettings
        );
    }
}
