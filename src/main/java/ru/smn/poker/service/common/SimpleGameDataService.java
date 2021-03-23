package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.Table;
import ru.smn.poker.service.GameDataService;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SimpleGameDataService implements GameDataService {

    private final Map<String, Table> games;

    @Override
    public Optional<PlayerEntity> getPlayerByName(String name) {
        return games.values().stream()
                .flatMap(game -> game.getPlayers().stream())
                .filter(player -> player.getName().equals(name))
                .findAny();
    }

    @Override
    public Table getGameByName(String gameName) {
        if (gameName == null) {
            throw new RuntimeException("game is null");
        }
        final Table table = games.get(gameName);
        if (table == null) {
            throw new RuntimeException("cannot find game");
        }
        return table;
    }

}
