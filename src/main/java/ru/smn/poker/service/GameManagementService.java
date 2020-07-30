package ru.smn.poker.service;

import ru.smn.poker.dto.Player;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.Game;
import ru.smn.poker.entities.GameEntity;

import java.util.List;

public interface GameManagementService {

    Game createGame(List<Player> players,
                    GameType gameType,
                    OrderService orderService,
                    long gameId);

    void createNewGame(GameType gameType,
                       OrderService orderService);

    void createNewGame(List<Player> players,
                       GameType gameType,
                       OrderService orderService);

    void restoreGame(GameEntity gameEntity);


    void startGame(Game game);


    void stopGame(Game game);

    void addListener(Runnable runnable);
}
