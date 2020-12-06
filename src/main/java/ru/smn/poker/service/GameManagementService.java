package ru.smn.poker.service;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.Game;

import java.util.List;

public interface GameManagementService {

    Game createGame(List<PlayerEntity> players,
                    GameType gameType,
                    OrderService orderService,
                    long gameId);

    void createNewGame(GameType gameType,
                       OrderService orderService);

    void createNewGame(List<PlayerEntity> players,
                       GameType gameType,
                       OrderService orderService);

    void startGame(Game game);

    void createNewGameTest(
            List<PlayerEntity> players,
            GameType gameType
    );

    void addListener(Runnable runnable);
}
