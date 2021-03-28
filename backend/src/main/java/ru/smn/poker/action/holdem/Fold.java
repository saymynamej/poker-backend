package ru.smn.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.smn.poker.action.ExecutableAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

import static ru.smn.poker.data.StrategyData.FOLD_STRATEGIES;

@RequiredArgsConstructor
@Getter
@ToString
public class Fold implements ExecutableAction {

    private final long count = 0;

    @Override
    public ActionType getActionType() {
        return ActionType.FOLD;
    }

    @Override
    public void doAction(TableSettings tableSettings, PlayerEntity player, GameService gameService, ActionService actionService) {
        FOLD_STRATEGIES.get(player.getTableSettings().getRoleType()).execute(
                player,
                gameService,
                actionService,
                this,
                tableSettings
        );
    }
}
