package ru.smn.poker.action.holdem;

import lombok.RequiredArgsConstructor;
import ru.smn.poker.action.ExecutableAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;
import ru.smn.poker.data.StrategyData;

@RequiredArgsConstructor
public class AllIn implements ExecutableAction {

    private final long count;

    @Override
    public ActionType getActionType() {
        return ActionType.ALLIN;
    }

    @Override
    public void doAction(RoundSettings roundSettings, PlayerEntity player, GameService gameService, ActionService actionService) {
        StrategyData.ALLIN_STRATEGIES.get(player.getRoleType()).execute(
                player,
                gameService,
                actionService,
                this,
                roundSettings
        );
    }

    @Override
    public long getCount() {
        return count;
    }
}
