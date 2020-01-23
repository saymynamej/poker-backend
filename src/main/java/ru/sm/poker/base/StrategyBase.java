package ru.sm.poker.base;

import ru.sm.poker.action.ActionStrategy;

import ru.sm.poker.action.holdem.strategy.*;
import ru.sm.poker.enums.RoleType;

import java.util.HashMap;
import java.util.Map;

public class StrategyBase {

    public final static Map<RoleType, ActionStrategy> CALL_STRATEGIES = new HashMap<>();
    public final static Map<RoleType, ActionStrategy> BET_STRATEGIES = new HashMap<>();
    public final static Map<RoleType, ActionStrategy> RAISE_STRATEGIES = new HashMap<>();
    public final static Map<RoleType, ActionStrategy> CHECK_STRATEGIES = new HashMap<>();
    public final static Map<RoleType, ActionStrategy> ALLIN_STRATEGIES = new HashMap<>();

    static {
        CALL_STRATEGIES.put(RoleType.BIG_BLIND, new CallStrategy());
        CALL_STRATEGIES.put(RoleType.SMALL_BLIND, new CallStrategy());
        CALL_STRATEGIES.put(RoleType.PLAYER, new CallStrategy());
        CALL_STRATEGIES.put(RoleType.BUTTON, new CallStrategy());
    }

    static {
        BET_STRATEGIES.put(RoleType.BIG_BLIND, new BetStrategy());
        BET_STRATEGIES.put(RoleType.SMALL_BLIND, new BetStrategy());
        BET_STRATEGIES.put(RoleType.PLAYER, new BetStrategy());
        BET_STRATEGIES.put(RoleType.BUTTON, new BetStrategy());
    }

    static {
        RAISE_STRATEGIES.put(RoleType.BIG_BLIND, new RaiseStrategy());
        RAISE_STRATEGIES.put(RoleType.SMALL_BLIND, new RaiseStrategy());
        RAISE_STRATEGIES.put(RoleType.PLAYER, new RaiseStrategy());
        RAISE_STRATEGIES.put(RoleType.BUTTON, new RaiseStrategy());
    }

    static {
        CHECK_STRATEGIES.put(RoleType.BIG_BLIND, new CheckStrategy());
        CHECK_STRATEGIES.put(RoleType.SMALL_BLIND, new CheckStrategy());
        CHECK_STRATEGIES.put(RoleType.PLAYER, new CheckStrategy());
        CHECK_STRATEGIES.put(RoleType.BUTTON, new CheckStrategy());
    }

    static {
        ALLIN_STRATEGIES.put(RoleType.BIG_BLIND, new AllStrategy());
        ALLIN_STRATEGIES.put(RoleType.SMALL_BLIND, new AllStrategy());
        ALLIN_STRATEGIES.put(RoleType.PLAYER, new AllStrategy());
        ALLIN_STRATEGIES.put(RoleType.BUTTON, new AllStrategy());
    }
}
