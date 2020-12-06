package ru.smn.poker.service;

import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.Game;

import java.util.Optional;

public interface GameDataService {
    Optional<PlayerEntity> getPlayerByName(String name);

    Game getGameByName(String gameName);
}
