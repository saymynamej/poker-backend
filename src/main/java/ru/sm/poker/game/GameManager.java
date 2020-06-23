package ru.sm.poker.game;

import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.service.OrderService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GameManager {

    Optional<PlayerDTO> getPlayerByName(String name);

    void reloadByName(String gameName);

    void disableByName(String gameName);

    void enableByName(String gameName);

    Game createGame(List<PlayerDTO> players, GameType gameType, OrderService orderService, boolean needRun);

    void addChips(String name);

    Game getGameByName(String gameName);

    Map<String, Game> getGames();

}
