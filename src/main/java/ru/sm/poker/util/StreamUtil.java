package ru.sm.poker.util;

import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;

import java.util.function.Predicate;

public class StreamUtil {

    public static Predicate<? super Player> playerInAllIn(){
        return (Predicate<Player>) player -> player.getAction().getActionType() == ActionType.ALLIN;
    }

    public static Predicate<? super Player> playerHasMoreThanZeroChips(){
        return (Predicate<Player>) player -> player.getChipsCount() > 0;
    }

    public static Predicate<? super Player> playersHasCheck(){
        return (Predicate<Player>) player -> player.getAction().getActionType() == ActionType.CHECK;
    }
}
