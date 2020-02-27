package ru.sm.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.data.StrategyData.RAISE_STRATEGIES;

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
    public void doAction(HoldemRoundSettings holdemRoundSettings, Player player, GameService gameService, ActionService actionService) {
        RAISE_STRATEGIES.get(player.getRoleType()).execute
                (
                        player,
                        gameService,
                        actionService,
                        this,
                        holdemRoundSettings
                );
    }
}
