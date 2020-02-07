package ru.sm.poker.util;

import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.enums.StateType;

import java.util.function.Predicate;

public class StreamUtil {

    public static Predicate<? super PlayerDTO> playerInAllIn() {
        return (Predicate<PlayerDTO>) player -> player.getAction().getActionType() == ActionType.ALLIN;
    }

    public static Predicate<? super PlayerDTO> playerHasMoreThanZeroChips() {
        return (Predicate<PlayerDTO>) player -> player.getChipsCount() > 0;
    }

    public static Predicate<? super PlayerDTO> playersHasCheck() {
        return (Predicate<PlayerDTO>) player -> player.getAction().getActionType() == ActionType.CHECK;
    }

    public static Predicate<? super PlayerDTO> playerIsNotAfk() {
        return (Predicate<PlayerDTO>) player -> player.getStateType() != StateType.AFK;
    }

    public static Predicate<? super PlayerDTO> playerInGame() {
        return (Predicate<PlayerDTO>) player -> player.getAction() != null &&
                player.getAction().getActionType() != ActionType.FOLD &&
                player.getRoleType() != null &&
                player.getStateType() != StateType.AFK &&
                player.getStateType() != StateType.LEAVE &&
                player.getCards() != null;
    }
}
