package ru.sm.poker.game;

import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Bet;

import java.util.List;

public interface Game {

    void startGame();

    List<Player> getPlayers();

    boolean addPlayer(Player player);

    void addPlayers(List<Player> players);

    void removePlayer(Player player);

    boolean isRunning();

    Player getPlayerByName(String name);

    Player getActivePlayer();

    Bet getLastBet();

}
