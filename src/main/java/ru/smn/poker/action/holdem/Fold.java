package ru.smn.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.action.ExecutableAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;

import static ru.smn.poker.data.StrategyData.CHECK_STRATEGIES;
import static ru.smn.poker.data.StrategyData.FOLD_STRATEGIES;

@RequiredArgsConstructor
@Getter
@ToString
public class Fold implements ExecutableAction {

    @Override
    public ActionType getActionType() {
        return ActionType.FOLD;
    }

    @Override
    public void doAction(RoundSettings roundSettings, PlayerEntity player, GameService gameService, ActionService actionService) {
        FOLD_STRATEGIES.get(player.getRoleType()).execute(
                player,
                gameService,
                actionService,
                this,
                roundSettings
        );
    }

    @Override
    public long getCount() {
        return 0;
    }
}
