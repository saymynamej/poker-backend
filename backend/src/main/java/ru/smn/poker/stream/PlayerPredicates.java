package ru.smn.poker.stream;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.enums.StateType;

import java.util.function.Predicate;

public class PlayerPredicates {

    public static Predicate<? super PlayerEntity> playerInAllIn() {
        return (Predicate<PlayerEntity>) player -> player.getAction().getActionType() == ActionType.ALLIN;
    }

    public static Predicate<? super PlayerEntity> playersHasCheck() {
        return (Predicate<PlayerEntity>) player -> player.getAction().getActionType() == ActionType.CHECK;
    }

    public static Predicate<? super PlayerEntity> playerFolded() {
        return (Predicate<PlayerEntity>) player -> player.getAction().getActionType() == ActionType.FOLD;
    }

    public static Predicate<? super PlayerEntity> playerNotFolded() {
        return (Predicate<PlayerEntity>) player -> player.getAction().getActionType() != ActionType.FOLD;
    }

    public static Predicate<? super PlayerEntity> playerCanMakeMove() {
        return (Predicate<PlayerEntity>) player -> player.getAction().getActionType() != ActionType.FOLD && player.getTableSettings().getStateType() == StateType.IN_GAME;
    }


    public static Predicate<? super PlayerEntity> playerHasChips(){
        return (Predicate<PlayerEntity>) player -> player.getTableSettings().getChipsCount().getCount() > 0;
    }

    public static Predicate<? super PlayerEntity> playerInGame() {
        return (Predicate<PlayerEntity>) player -> player.getTableSettings().getStateType() == StateType.IN_GAME;
    }
}
