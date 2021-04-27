package ru.smn.poker.service;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.enums.GameType;

import java.util.List;

public interface GameTableCreator {

    void create(int countOfPlayers, GameType gameType);

    void restore(TableEntity tableEntity);

    void create(
            List<PlayerEntity> players,
            String tableName,
            GameType gameType
    );

    void restoreAll();
}
