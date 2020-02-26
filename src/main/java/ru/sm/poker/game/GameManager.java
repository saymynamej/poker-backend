package ru.sm.poker.game;

import ru.sm.poker.dto.Player;

import java.util.Map;
import java.util.Optional;

public interface GameManager {

    boolean playerExistByName(String gameName, String name);

    Optional<Player> getPlayerByName(String name);

    void removePlayer(String gameName, Player player);

    void removePlayer(Player player);

    void reload(String gameName);

    void disableGame(String gameName);

    void enableGame(String gameName);

    void startGame(String gameName);

    void createNewGame(String name, Game game);

    void addChips(String name);

    void addChips(String name, long count);

    Player getActivePlayerInGame(String game);

    Game getGameByName(String gameName);

    Map<String, Game> getGames();

}
