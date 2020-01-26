package ru.sm.poker.util;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.Player;

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
        return Player.builder()
                .name(playerName)
                .timeBank(200L)
                .chipsCount(5000L)
                .build();
    }

    public static List<Player> getPlayersInGame(List<Player> players) {
        return players.stream()
                .filter(player -> player.getAction() != null &&
                        player.getAction().getActionType() != ActionType.FOLD &&
                        player.getRoleType() != null &&
                        player.getStateType() != StateType.AFK &&
                        player.getCards() != null
                )
                .collect(Collectors.toList());
    }

    public static boolean checkPlayerHasEnoughChips(Player player, CountAction countAction) {
        return player.getChipsCount() >= countAction.getCount();
    }
}
