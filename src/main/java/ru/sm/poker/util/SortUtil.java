package ru.sm.poker.util;

import ru.sm.poker.enums.RoleType;
import ru.sm.poker.model.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SortUtil {

    public static List<Player> sortPreflop(List<Player> players) {

        final List<Player> sortedPlayers = new ArrayList<>(players);

        final Optional<Player> bigBlind = getPlayerByRole(players, RoleType.BIG_BLIND);
        final Optional<Player> smallBlind = getPlayerByRole(players, RoleType.SMALL_BLIND);
        final Optional<Player> button = getPlayerByRole(players, RoleType.BUTTON);

        if (bigBlind.isEmpty() || smallBlind.isEmpty() || button.isEmpty()) {
            throw new RuntimeException("cannot find all roles for preflop");
        }

        sortedPlayers.remove(bigBlind.get());
        sortedPlayers.remove(smallBlind.get());
        sortedPlayers.remove(button.get());
        sortedPlayers.add(button.get());
        sortedPlayers.add(smallBlind.get());
        sortedPlayers.add(bigBlind.get());

        return sortedPlayers;
    }


    public static List<Player> sortPostflop(List<Player> players) {
        final List<Player> sortedList = new LinkedList<>(players);

        final Optional<Player> smallBlind = getPlayerByRole(players, RoleType.SMALL_BLIND);
        final Optional<Player> bigBlind = getPlayerByRole(players, RoleType.BIG_BLIND);
        final Optional<Player> button = getPlayerByRole(players, RoleType.BUTTON);

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


    private static Optional<Player> getPlayerByRole(List<Player> players, RoleType roleType) {
        return players
                .stream()
                .filter(player -> player.getRoleType() == roleType)
                .findFirst();
    }

}
