package ru.smn.poker.service;

import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.Game;

import java.util.List;

public interface GameManagementService {

    void createEmptyGame(GameType gameType);

    void create(List<PlayerEntity> players,
                GameType gameType,
                long defaultChipsCount
    );

    void run(Game game);

    void create(int countOfPlayers, long defaultChipsCount, GameType gameType);

    void run(GameEntity gameEntity);

    void addListener(Runnable runnable);

}
