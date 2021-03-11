package ru.smn.poker.action.holdem;

import lombok.RequiredArgsConstructor;
import ru.smn.poker.action.ExecutableAction;
import ru.smn.poker.data.StrategyData;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;

@RequiredArgsConstructor
public class AllIn implements ExecutableAction {

    private final long count;

    @Override
    public ActionType getActionType() {
        return ActionType.ALLIN;
    }

    @Override
    public void doAction(TableSettings tableSettings, PlayerEntity player, GameService gameService, ActionService actionService) {
        StrategyData.ALLIN_STRATEGIES.get(player.getRoleType()).execute(
                player,
                gameService,
                actionService,
                this,
                tableSettings
        );
    }

    @Override
    public long getCount() {
        return count;
    }
}
