package ru.sm.poker.service;

import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.game.Game;

import java.util.Optional;

public interface GameDataService {
    Optional<PlayerDTO> getPlayerByName(String name);

    Game getGameByName(String gameName);
}
