package ru.smn.poker.util;

import ru.smn.poker.dto.Player;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.enums.StateType;

import java.util.function.Predicate;

public class StreamUtil {

    public static Predicate<? super Player> playerInAllIn() {
        return (Predicate<Player>) player -> player.getAction().getActionType() == ActionType.ALLIN;
    }

    public static Predicate<? super Player> playersHasCheck() {
        return (Predicate<Player>) player -> player.getAction().getActionType() == ActionType.CHECK;
    }

    public static Predicate<? super Player> playerFolded() {
        return (Predicate<Player>) player -> player.getAction().getActionType() == ActionType.FOLD;
    }

    public static Predicate<? super Player> playerHasChips(){
        return (Predicate<Player>) player -> player.getChipsCount() > 0;
    }

    public static Predicate<? super Player> playerInGame() {
        return (Predicate<Player>) player -> player.getStateType() == StateType.IN_GAME;
    }
}
