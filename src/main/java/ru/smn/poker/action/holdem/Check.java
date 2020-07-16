package ru.smn.poker.action.holdem;

import lombok.Getter;
import lombok.ToString;
import ru.smn.poker.action.ExecutableAction;
import ru.smn.poker.dto.Player;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

import static ru.smn.poker.data.StrategyData.CHECK_STRATEGIES;

@ToString
public class Check implements ExecutableAction {

    @Getter
    private final long count = 0;

    @Override
    public ActionType getActionType() {
        return ActionType.CHECK;
    }

    @Override
    public void doAction(RoundSettings roundSettings, Player player, GameService gameService, ActionService actionService) {
        CHECK_STRATEGIES.get(player.getRoleType()).execute(
                player,
                gameService,
                actionService,
                this,
                roundSettings
        );
    }
}
