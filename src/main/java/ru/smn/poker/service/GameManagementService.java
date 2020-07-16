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

    void startGame(String gameName);

    Game restoreGame(GameEntity gameEntity);

    Game restoreGame(Game game);

    GameEntity saveGame(Game game);

    void startGame(Game game);

    void stopGame(String gameName);

    void stopGame(Game game);

    void addListener(Runnable runnable);
}
