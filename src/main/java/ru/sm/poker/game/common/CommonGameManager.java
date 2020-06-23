package ru.sm.poker.game.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.data.GameManagement;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.game.Round;
import ru.sm.poker.game.holdem.HoldemGame;
import ru.sm.poker.game.holdem.HoldemRound;
import ru.sm.poker.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static ru.sm.poker.util.GameUtil.getRandomGOTCityName;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonGameManager implements GameManager {
    private final static Map<String, Game> games = new ConcurrentHashMap<>();
    private final Map<GameType, GameSettings> mapSettings;
    private final GameManagement gameManagement;

    @Override
    public Optional<PlayerDTO> getPlayerByName(String name) {
        return games.values().stream()
                .flatMap(game -> game.getPlayers().stream())
                .filter(player -> player.getName().equals(name))
                .findAny();
    }

    @Override
    public Game createGame(List<PlayerDTO> players, GameType gameType, OrderService orderService, boolean needRun) {
        final String randomGameName = getRandomGOTCityName();

        final GameSettings gameSettings = mapSettings.get(gameType);

        final Round round = new HoldemRound(
                new ArrayList<>(players),
                randomGameName,
                orderService,
                gameSettings.getStartSmallBlindBet(),
                gameSettings.getStartBigBlindBet()
        );

        final Game game = new HoldemGame(
                gameSettings,
                round
        );

        if (checkGameName(randomGameName)) {
            games.put(randomGameName, game);
            log.info("game: " + randomGameName + " created");
        }
        if (needRun){
            gameManagement.startGame(game);
        }
        return game;
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

    @Override
    public Map<String, Game> getGames() {
        return games;
    }


    private boolean checkGameName(String gameName) {
        return !games.containsKey(gameName);
    }

}
