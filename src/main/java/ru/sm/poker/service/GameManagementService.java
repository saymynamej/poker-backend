package ru.sm.poker.service;

import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.Game;

import java.util.List;

public interface GameManagementService {
    Game createGame(List<PlayerDTO> players, GameType gameType, OrderService orderService, boolean needRun);

    void startGame(String gameName);

    void startGame(Game game);

    Game stopGame(String gameName);

    Game stopGame(Game game);

    void addListener(Runnable runnable);
}
