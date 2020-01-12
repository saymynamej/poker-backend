package ru.sm.poker.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.model.Player;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.action.strategy.call.CallBigBlindStrategy;
import ru.sm.poker.action.strategy.call.CallCommonStrategy;
import ru.sm.poker.action.strategy.call.CallSmallBlindStrategy;
import ru.sm.poker.service.ActionService;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class Call implements ExecutableAction {

    private final static Map<RoleType, ActionStrategy> strategies = new HashMap<>();
    private final long count;

    static {
        strategies.put(RoleType.BIG_BLIND, new CallBigBlindStrategy());
        strategies.put(RoleType.SMALL_BLIND, new CallSmallBlindStrategy());
        strategies.put(RoleType.PLAYER, new CallCommonStrategy());
        strategies.put(RoleType.BUTTON, new CallCommonStrategy());
    }

    @Override
    public ActionType getActionType() {
        return ActionType.CALL;
    }

    @Override
    public void doAction(RoundSettingsDTO roundSettingsDTO, Player player, ActionService actionService) {
        strategies.get(player.getRoleType()).execute(player, actionService, this, roundSettingsDTO);
    }
}
