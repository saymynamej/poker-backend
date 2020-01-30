package ru.sm.poker.util;

import ru.sm.poker.enums.RoleType;
import ru.sm.poker.dto.PlayerDTO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SortUtil {

    public static List<PlayerDTO> sortPreflop(List<PlayerDTO> playerDTOS) {

        final List<PlayerDTO> sortedPlayerDTOS = new ArrayList<>(playerDTOS);

        final Optional<PlayerDTO> bigBlind = getPlayerByRole(playerDTOS, RoleType.BIG_BLIND);
        final Optional<PlayerDTO> smallBlind = getPlayerByRole(playerDTOS, RoleType.SMALL_BLIND);
        final Optional<PlayerDTO> button = getPlayerByRole(playerDTOS, RoleType.BUTTON);

        if (bigBlind.isEmpty() || smallBlind.isEmpty() || button.isEmpty()) {
            throw new RuntimeException("cannot find all roles for preflop");
        }

        sortedPlayerDTOS.remove(bigBlind.get());
        sortedPlayerDTOS.remove(smallBlind.get());
        sortedPlayerDTOS.remove(button.get());
        sortedPlayerDTOS.add(button.get());
        sortedPlayerDTOS.add(smallBlind.get());
        sortedPlayerDTOS.add(bigBlind.get());

        return sortedPlayerDTOS;
    }


    public static List<PlayerDTO> sortPostflop(List<PlayerDTO> playerDTOS) {
        final List<PlayerDTO> sortedList = new LinkedList<>(playerDTOS);

        final Optional<PlayerDTO> smallBlind = getPlayerByRole(playerDTOS, RoleType.SMALL_BLIND);
        final Optional<PlayerDTO> bigBlind = getPlayerByRole(playerDTOS, RoleType.BIG_BLIND);
        final Optional<PlayerDTO> button = getPlayerByRole(playerDTOS, RoleType.BUTTON);

        if (smallBlind.isPresent()) {
            sortedList.remove(smallBlind.get());
            sortedList.add(0, smallBlind.get());
        }
        if (bigBlind.isPresent()) {
            sortedList.remove(bigBlind.get());
            sortedList.add(1, bigBlind.get());
        }
        if (button.isPresent()) {
            sortedList.remove(button.get());
            sortedList.add(button.get());
        }

        return sortedList;
    }


    private static Optional<PlayerDTO> getPlayerByRole(List<PlayerDTO> playerDTOS, RoleType roleType) {
        return playerDTOS
                .stream()
                .filter(player -> player.getRoleType() == roleType)
                .findFirst();
    }

}
