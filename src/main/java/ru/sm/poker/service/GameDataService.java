package ru.sm.poker.service;

import ru.sm.poker.dto.Player;
import ru.sm.poker.game.Game;

import java.util.Optional;

public interface GameDataService {
    Optional<Player> getPlayerByName(String name);

    Game getGameByName(String gameName);
}
