package ru.sm.poker.service;

import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.Game;
import ru.sm.poker.entities.GameEntity;

import java.util.List;

public interface GameManagementService {

    Game createGame(List<Player> players,
                    GameType gameType,
                    OrderService orderService);

    void startGame(String gameName);

    Game restoreGame(GameEntity gameEntity);

    void saveGame(Game game);

    void startGame(Game game);

    void stopGame(String gameName);

    void stopGame(Game game);

    void addListener(Runnable runnable);
}
