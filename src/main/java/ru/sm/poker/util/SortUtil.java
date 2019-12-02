package ru.sm.poker.util;

import ru.sm.poker.enums.RoleType;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Call;
import ru.sm.poker.model.action.Raise;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SortUtil {

    public static List<Player> sortPre(List<Player> players) {
        return sortPreflop(players);
    }


    public static List<Player> sortPost(List<Player> players) {
        return sortPostFlop(players);
    }

    private static List<Player> sortPreflop(List<Player> players) {
        final List<Player> sortedPlayers = new ArrayList<>();
        final Player bigBlind = getPlayerByRole(players, RoleType.BIG_BLIND).get();
        final Player smallBlind = getPlayerByRole(players, RoleType.SMALL_BLIND).get();
        final Player button = getPlayerByRole(players, RoleType.BUTTON).get();
        final List<Player> afterBigBlind = takeAllAfterBigBlind(players);
        final List<Player> beforeButton = takeAllBeforeButton(players);
        sortedPlayers.addAll(afterBigBlind);
        sortedPlayers.addAll(beforeButton);
        sortedPlayers.add(button);
        sortedPlayers.add(smallBlind);
        sortedPlayers.add(bigBlind);
        return sortedPlayers
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }


    private static List<Player> sortPostFlop(List<Player> players) {
        List<Player> sortedList = new ArrayList<>();
        final Optional<Player> smallBlind = getPlayerByRole(players, RoleType.SMALL_BLIND);
        smallBlind.ifPresent(sortedList::add);
        final Optional<Player> bigBlind = getPlayerByRole(players, RoleType.BIG_BLIND);
        bigBlind.ifPresent(sortedList::add);
        return sortedList;
    }


    private static Optional<Player> getPlayerByRole(List<Player> players, RoleType roleType) {
        return players
                .stream()
                .filter(player -> player.getRoleType() == roleType)
                .findFirst();
    }

    private static List<Player> takeAllBeforeButton(List<Player> players) {
        final List<Player> beforeButtonList = new ArrayList<>();
        for (Player player : players) {
            if (player.getRoleType() != RoleType.SMALL_BLIND
                    && player.getRoleType() != RoleType.BIG_BLIND && player.getRoleType() != RoleType.BUTTON)
                beforeButtonList.add(player);
            if (player.getRoleType() == RoleType.BUTTON) {
                break;
            }
        }
        return beforeButtonList;
    }

    private static List<Player> takeAllAfterBigBlind(List<Player> players) {
        final List<Player> afterBigBlindList = new ArrayList<>();
        boolean startCollect = false;
        for (Player player : players) {
            if (player.getRoleType() == RoleType.BIG_BLIND) {
                startCollect = true;
                continue;
            }
            if (startCollect
                    && player.getRoleType() != RoleType.BUTTON
                    && player.getRoleType() != RoleType.SMALL_BLIND) {
                afterBigBlindList.add(player);
            }
        }
        return afterBigBlindList;
    }


    private static List<Player> getPlayersInGame(List<Player> players) {
        return players
                .stream()
                .filter(player -> player.getAction() instanceof Call || player.getAction() instanceof Raise)
                .collect(Collectors.toList());
    }
}
