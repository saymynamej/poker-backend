package ru.smn.poker.data;

import ru.smn.poker.action.ActionStrategy;
import ru.smn.poker.action.holdem.strategy.*;
import ru.smn.poker.enums.RoleType;

import java.util.HashMap;
import java.util.Map;

public class StrategyData {

    public final static Map<RoleType, ActionStrategy> CALL_STRATEGIES = new HashMap<>();
    public final static Map<RoleType, ActionStrategy> BET_STRATEGIES = new HashMap<>();
    public final static Map<RoleType, ActionStrategy> RAISE_STRATEGIES = new HashMap<>();
    public final static Map<RoleType, ActionStrategy> CHECK_STRATEGIES = new HashMap<>();
    public final static Map<RoleType, ActionStrategy> ALLIN_STRATEGIES = new HashMap<>();

    static {
        CALL_STRATEGIES.put(RoleType.BIG_BLIND, new CallStrategy());
        CALL_STRATEGIES.put(RoleType.SMALL_BLIND, new CallStrategy());
        CALL_STRATEGIES.put(RoleType.ORDINARY, new CallStrategy());
        CALL_STRATEGIES.put(RoleType.BUTTON, new CallStrategy());
    }

    static {
        BET_STRATEGIES.put(RoleType.BIG_BLIND, new BetStrategy());
        BET_STRATEGIES.put(RoleType.SMALL_BLIND, new BetStrategy());
        BET_STRATEGIES.put(RoleType.ORDINARY, new BetStrategy());
        BET_STRATEGIES.put(RoleType.BUTTON, new BetStrategy());
    }

    static {
        RAISE_STRATEGIES.put(RoleType.BIG_BLIND, new RaiseStrategy());
        RAISE_STRATEGIES.put(RoleType.SMALL_BLIND, new RaiseStrategy());
        RAISE_STRATEGIES.put(RoleType.ORDINARY, new RaiseStrategy());
        RAISE_STRATEGIES.put(RoleType.BUTTON, new RaiseStrategy());
    }

    static {
        CHECK_STRATEGIES.put(RoleType.BIG_BLIND, new CheckStrategy());
        CHECK_STRATEGIES.put(RoleType.SMALL_BLIND, new CheckStrategy());
        CHECK_STRATEGIES.put(RoleType.ORDINARY, new CheckStrategy());
        CHECK_STRATEGIES.put(RoleType.BUTTON, new CheckStrategy());
    }

    static {
        ALLIN_STRATEGIES.put(RoleType.BIG_BLIND, new AllStrategy());
        ALLIN_STRATEGIES.put(RoleType.SMALL_BLIND, new AllStrategy());
        ALLIN_STRATEGIES.put(RoleType.ORDINARY, new AllStrategy());
        ALLIN_STRATEGIES.put(RoleType.BUTTON, new AllStrategy());
    }
}
