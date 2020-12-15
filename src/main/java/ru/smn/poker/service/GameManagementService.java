package ru.smn.poker.service;

import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.Game;

import java.util.List;

public interface GameManagementService {

    Game createGame(List<PlayerEntity> players,
                    GameType gameType,
                    OrderService orderService,
                    long gameId);

    void createGame(GameType gameType,
                    OrderService orderService);

    void createGame(List<PlayerEntity> players,
                    GameType gameType,
                    OrderService orderService);

    void startGame(Game game);

    void createGame(int countOfPlayers, long defaultChipsCount);

    void startGame(GameEntity gameEntity);

    void createGame(
            List<PlayerEntity> players,
            GameType gameType
    );

    void addListener(Runnable runnable);
}
