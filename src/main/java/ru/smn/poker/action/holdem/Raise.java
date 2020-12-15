package ru.smn.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.action.ExecutableAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;

import static ru.smn.poker.data.StrategyData.RAISE_STRATEGIES;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Raise implements ExecutableAction, CountAction {

    private final long count;

    @Override
    public ActionType getActionType() {
        return ActionType.RAISE;
    }

    @Override
    public void doAction(RoundSettings roundSettings, PlayerEntity player, GameService gameService, ActionService actionService) {
        RAISE_STRATEGIES.get(player.getRoleType()).execute
                (
                        player,
                        gameService,
                        actionService,
                        this,
                        roundSettings
                );
    }
}
