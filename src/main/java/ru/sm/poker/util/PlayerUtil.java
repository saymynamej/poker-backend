package ru.sm.poker.util;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerUtil {

    public static List<Player> copies(List<Player> players) {
        final List<Player> copyPlayers = new ArrayList<>();
        players.forEach(player -> copyPlayers.add(player.copy()));
        return copyPlayers;
    }

    public static Player getDefaultPlayerForHoldem(String playerName) {
        return getDefaultPlayerForHoldem(playerName, 5000L);
    }

    public static Player getDefaultPlayerForHoldem(String playerName, long chipsCount) {
        return Player.builder()
                .name(playerName)
                .timeBank(5L)
                .chipsCount(chipsCount)
                .build();
    }

    public static List<Player> getPlayersInGame(List<Player> players) {
        return players.stream()
                .filter(StreamUtil.playerInGame())
                .collect(Collectors.toList());
    }

    public static boolean hasPlayerEnoughChips(Player player, CountAction countAction) {
        return player.getChipsCount() >= countAction.getCount();
    }

}
