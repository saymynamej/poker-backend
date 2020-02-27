package ru.sm.poker.game.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.Player;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommonGameManager implements GameManager {

    private final static Map<String, Game> games = new ConcurrentHashMap<>();
    private final static Map<Player, Long> CHIPS_MAP = new ConcurrentHashMap<>();

    @Override
    public Optional<Player> getPlayerByName(String name) {
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
    public void addChips(Player player, long count) {
        if (player == null) {
            return;
        }
        player.addChips(count);
    }

    @Override
    public void addChips(String name) {
        final Optional<Player> optionalPlayer = getPlayerByName(name);
        if (optionalPlayer.isEmpty()) {
            return;
        }
        final Player player = optionalPlayer.get();
        CHIPS_MAP.put(player, 5000L);
    }

    @Override
    public void removePlayer(String gameName, Player player) {
        final Game game = games.get(gameName);
        if (game != null) {
            game.removePlayer(player);
            log.info("Player removed :" + player.getName());
            return;
        }
        throw new RuntimeException(format("could not found player in game =%s, player=%s", gameName, player.getName()));
    }

    @Override
    public void createNewGame(String name, Game game) {
        if (checkGameName(name)) {
            games.put(name, game);
            log.info("game: " + name + " created");
        }
    }

    @Override
    public void removePlayer(Player player) {
        games.forEach((name, game) -> {
            final List<Player> players = game.getRoundSettings().getPlayers();
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
    public Player getActivePlayerInGame(String game) {
        return getGameByName(game).getRoundSettings()
                .getPlayers()
                .stream()
                .filter(Player::isActive)
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

    public static Map<Player, Long> getChipsMap() {
        return CHIPS_MAP;
    }

    private void addChipsToMap(Player player, Long chips) {
        if (CHIPS_MAP.containsKey(player)) {
            return;
        }
        CHIPS_MAP.put(player, chips);
    }

    private boolean checkGameName(String gameName) {
        return !games.containsKey(gameName);
    }

}
