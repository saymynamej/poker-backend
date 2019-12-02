package ru.sm.poker.oldgame;

import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Bet;

import java.util.List;
import java.util.Optional;

public interface GameManager {

    boolean addPlayer(Player player);

    boolean playerExistByName(String name);

    void addPlayers(List<Player> players);

    void removePlayer(Player player);

    Optional<Player> getPlayerByName(String name);

    Player getActivePlayer();
}
