package ru.smn.poker.util;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.enums.StateType;

import java.util.function.Predicate;

public class StreamUtil {

    public static Predicate<? super PlayerEntity> playerInAllIn() {
        return (Predicate<PlayerEntity>) player -> player.getAction().getActionType() == ActionType.ALLIN;
    }

    public static Predicate<? super PlayerEntity> playersHasCheck() {
        return (Predicate<PlayerEntity>) player -> player.getAction().getActionType() == ActionType.CHECK;
    }

    public static Predicate<? super PlayerEntity> playerFolded() {
        return (Predicate<PlayerEntity>) player -> player.getAction().getActionType() == ActionType.FOLD;
    }

    public static Predicate<? super PlayerEntity> playerHasChips(){
        return (Predicate<PlayerEntity>) player -> player.getSettings().getChipsCount().getCount() > 0;
    }

    public static Predicate<? super PlayerEntity> playerInGame() {
        return (Predicate<PlayerEntity>) player -> player.getSettings().getStateType() == StateType.IN_GAME;
    }
}
