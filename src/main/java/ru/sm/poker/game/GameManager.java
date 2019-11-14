package ru.sm.poker.game;

import ru.sm.poker.model.Player;
import java.util.Map;

public interface GameManager {
    boolean playerExistByName(String gameName, String name);
    Map<String, Player> getPlayerByName(String name);
    void removePlayer(String gameName, Player player);
    void removePlayer(Player player);
    void addPlayer(Player player);
}
