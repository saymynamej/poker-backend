package ru.sm.poker.game.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.sm.poker.config.game.GameSettings;
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

import static java.lang.String.format;
import static ru.sm.poker.util.GameUtil.getRandomGOTCityName;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonGameManager implements GameManager {
    private final static Map<String, Game> games = new ConcurrentHashMap<>();
    private final static Map<PlayerDTO, Long> CHIPS_MAP = new ConcurrentHashMap<>();
    private final Map<GameType, GameSettings> mapSettings;


    @Override
    public Optional<PlayerDTO> getPlayerByName(String name) {
        return games.values().stream()
                .flatMap(game -> game.getPlayers().stream())
                .filter(player -> player.getName().equals(name))
                .findAny();
    }

    @Override
    public boolean playerExistByName(String gameName, String name) {
        final Game game = games.get(gameName);
        return game.getPlayers().stream()
                .anyMatch(player -> player.getName().equals(name));
    }


    @Override
    public void addChips(PlayerDTO player, long count) {
        if (player == null) {
            return;
        }
        player.addChips(count);
    }

    @Override
    public void addChips(String name) {
        final Optional<PlayerDTO> optionalPlayer = getPlayerByName(name);
        if (optionalPlayer.isEmpty()) {
            return;
        }
        final PlayerDTO player = optionalPlayer.get();
        CHIPS_MAP.put(player, 5000L);
    }

    @Override
    public void removePlayer(String gameName, PlayerDTO player) {
        final Game game = games.get(gameName);
        if (game != null) {
            game.removePlayer(player);
            log.info("Player removed :" + player.getName());
            return;
        }
        throw new RuntimeException(format("could not found player in game =%s, player=%s", gameName, player.getName()));
    }

    @Override
    public Game createGame(List<PlayerDTO> players, GameType gameType, OrderService orderService) {
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
        return game;
    }

    @Override
    public void removePlayer(PlayerDTO player) {
        games.forEach((name, game) -> {
            final List<PlayerDTO> players = game.getRoundSettings().getPlayers();
            players.remove(player);
        });
    }

    @Override
    public void reload(String gameName) {
        getGameByName(gameName).reload();
    }


    @Override
    public void disableGame(String gameName) {
        getGameByName(gameName).disable();
    }

    @Override
    public void enableGame(String gameName) {
        getGameByName(gameName).enable();
    }

    @Override
    public void startGame(String gameName) {
        getGameByName(gameName).start();
    }

    @Override
    public PlayerDTO getActivePlayerInGame(String game) {
        return getGameByName(game).getRoundSettings()
                .getPlayers()
                .stream()
                .filter(PlayerDTO::isActive)
                .findFirst().orElseThrow(() -> new RuntimeException("cannot find active player in game:" + game));
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

    public static Map<PlayerDTO, Long> getChipsMap() {
        return CHIPS_MAP;
    }

    private void addChipsToMap(PlayerDTO player, Long chips) {
        if (CHIPS_MAP.containsKey(player)) {
            return;
        }
        CHIPS_MAP.put(player, chips);
    }

    private boolean checkGameName(String gameName) {
        return !games.containsKey(gameName);
    }

}
