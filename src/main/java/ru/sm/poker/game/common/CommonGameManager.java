package ru.sm.poker.game.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.PlayerDTO;
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
    public void addChips(String name, long count) {
        final Optional<PlayerDTO> optionalPlayer = getPlayerByName(name);
        if (optionalPlayer.isPresent()) {
            final PlayerDTO playerDTO = optionalPlayer.get();
            playerDTO.addChips(count);
        }
    }

    @Override
    public void addChips(String name) {
        addChips(name, 5000L);
    }

    @Override
    public void removePlayer(String gameName, PlayerDTO playerDTO) {
        final Game game = games.get(gameName);
        if (game != null) {
            game.removePlayer(playerDTO);
            log.info("Player removed :" + playerDTO.getName());
            return;
        }
        throw new RuntimeException(format("could not found player in game =%s, player=%s", gameName, playerDTO.getName()));
    }

    @Override
    public void createNewGame(String name, Game game) {
        if (checkGameName(name)) {
            games.put(name, game);
            log.info("game: " + name + " created");
        }
    }

    @Override
    public void removePlayer(PlayerDTO playerDTO) {
        games.forEach((name, game) -> {
            final List<PlayerDTO> playerDTOS = game.getRoundSettings().getPlayers();
            playerDTOS.remove(playerDTO);
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

    private boolean checkGameName(String gameName) {
        return !games.containsKey(gameName);
    }

}
