package ru.sm.poker.base;

import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.action.strategy.bet.BetCommonStrategy;
import ru.sm.poker.action.strategy.call.CallBigBlindStrategy;
import ru.sm.poker.action.strategy.call.CallCommonStrategy;
import ru.sm.poker.action.strategy.call.CallSmallBlindStrategy;
import ru.sm.poker.action.strategy.raise.RaiseCommonStrategy;
import ru.sm.poker.enums.RoleType;

import java.util.HashMap;
import java.util.Map;

public class StrategyBase {
    public final static Map<RoleType, ActionStrategy> CALL_STRATEGIES = new HashMap<>();
    public final static Map<RoleType, ActionStrategy> BET_STRATEGIES = new HashMap<>();
    public final static Map<RoleType, ActionStrategy> RAISE_STRATEGIES = new HashMap<>();
    public final static Map<RoleType, ActionStrategy> CHECK_STRATEGIES = new HashMap<>();

    static {
        CALL_STRATEGIES.put(RoleType.BIG_BLIND, new CallBigBlindStrategy());
        CALL_STRATEGIES.put(RoleType.SMALL_BLIND, new CallSmallBlindStrategy());
        CALL_STRATEGIES.put(RoleType.PLAYER, new CallCommonStrategy());
        CALL_STRATEGIES.put(RoleType.BUTTON, new CallCommonStrategy());
    }

    static {
        BET_STRATEGIES.put(RoleType.BIG_BLIND, new BetCommonStrategy());
        BET_STRATEGIES.put(RoleType.SMALL_BLIND, new BetCommonStrategy());
        BET_STRATEGIES.put(RoleType.PLAYER, new BetCommonStrategy());
        BET_STRATEGIES.put(RoleType.BUTTON, new BetCommonStrategy());
    }

    static {
        RAISE_STRATEGIES.put(RoleType.BIG_BLIND, new RaiseCommonStrategy());
        RAISE_STRATEGIES.put(RoleType.SMALL_BLIND, new RaiseCommonStrategy());
        RAISE_STRATEGIES.put(RoleType.PLAYER, new RaiseCommonStrategy());
        RAISE_STRATEGIES.put(RoleType.BUTTON, new RaiseCommonStrategy());
    }

    static {
        CHECK_STRATEGIES.put(RoleType.BIG_BLIND, new BetCommonStrategy());
        CHECK_STRATEGIES.put(RoleType.SMALL_BLIND, new BetCommonStrategy());
        CHECK_STRATEGIES.put(RoleType.PLAYER, new BetCommonStrategy());
        CHECK_STRATEGIES.put(RoleType.BUTTON, new BetCommonStrategy());
    }
}
