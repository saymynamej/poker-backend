package ru.smn.poker.service;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.Table;

import java.util.Optional;

public interface GameDataService {
    Optional<PlayerEntity> getPlayerByName(String name);

    Table getGameByName(String gameName);
}
