package ru.sm.poker.game;

import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Bet;

import java.util.List;
import java.util.Optional;

public interface Game {

    void startGame();

    List<Player> getPlayers();

    boolean addPlayer(Player player);

    void addPlayers(List<Player> players);

    void removePlayer(Player player);

    boolean isRunning();

    Optional<Player> getPlayerByName(String name);

    Player getActivePlayer();

    Bet getLastBet();

    boolean playerExistByName(String name);

}
