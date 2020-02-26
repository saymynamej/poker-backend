package ru.sm.poker.util;

import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.ActionType;

import static ru.sm.poker.util.WaitUtil.waitOfCondition;

public class WaitUtilTest {

    public static void waitAction(Player player) {
       waitAction(player, ActionType.WAIT);
    }

    public static void waitAction(Player player, ActionType actionType) {
        waitOfCondition(() -> player.getAction() != null && player.getAction().getActionType() != actionType);
    }

}
