package ru.sm.poker.game;

import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.service.OrderService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GameManager {

    boolean playerExistByName(String gameName, String name);

    Optional<PlayerDTO> getPlayerByName(String name);

    void removePlayer(String gameName, PlayerDTO player);

    void removePlayer(PlayerDTO player);

    void reload(String gameName);

    void disableGame(String gameName);

    void enableGame(String gameName);

    void startGame(String gameName);

    Game createGame(List<PlayerDTO> players, GameType gameType, OrderService orderService);

    void addChips(String name);

    void addChips(PlayerDTO player, long count);

    PlayerDTO getActivePlayerInGame(String game);

    Game getGameByName(String gameName);

    Map<String, Game> getGames();

}
