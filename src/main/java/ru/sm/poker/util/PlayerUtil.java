package ru.sm.poker.util;

import ru.sm.poker.model.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtil {

    public static List<Player> copies(List<Player> players){
        final List<Player> copyPlayers = new ArrayList<>();
        players.forEach(player -> copyPlayers.add(player.copy()));
        return copyPlayers;
    }
}
