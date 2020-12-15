package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.Game;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonGameDataService implements GameDataService {

    private final Map<String, Game> games;

    @Override
    public Optional<PlayerEntity> getPlayerByName(String name) {
        return games.values().stream()
                .flatMap(game -> game.getPlayers().stream())
                .filter(player -> player.getName().equals(name))
                .findAny();
    }

    @Override
    public Game getGameByName(String gameName) {
        if (gameName == null) {
            throw new RuntimeException("game is null");
        }
        final Game game = games.get(gameName);
        if (game == null) {
            throw new RuntimeException("cannot find game");
        }
        return game;
    }

}
