package ru.smn.poker.service;

import ru.smn.poker.dto.Player;
import ru.smn.poker.game.Game;

import java.util.Optional;

public interface GameDataService {
    Optional<Player> getPlayerByName(String name);

    Game getGameByName(String gameName);
}
