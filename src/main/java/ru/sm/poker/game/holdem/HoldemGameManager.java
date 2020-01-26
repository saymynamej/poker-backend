package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.model.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
@Slf4j
public class HoldemGameManager implements GameManager {

    private final Map<String, Game> games;


    @Override
    public Optional<Pair<String, Player>> getPlayerByName(String name) {
        try {
            final Optional<Map.Entry<String, Player>> gameAndPlayer = games
                    .entrySet()
                    .stream()
                    .flatMap((entry -> entry
                            .getValue()
                            .getRoundSettings()
                            .getPlayers()
                            .stream()
                            .filter(player -> player.getName().equals(name))
                            .collect(Collectors.toMap(playerF -> entry.getKey(), playerF -> playerF))
                            .entrySet()
                            .stream()))
                    .findFirst();

            if (gameAndPlayer.isPresent()) {
                final Map.Entry<String, Player> player = gameAndPlayer.get();
                return Optional.of(Pair.of(player.getKey(), player.getValue()));
            }

        } catch (Exception ex) {
            log.info("user not found");
        }

        return Optional.empty();
    }


    @Override
    public boolean playerExistByName(String gameName, String name) {
        final Game game = games.get(gameName);
        return game.getRoundSettings()
                .getPlayers()
                .stream()
                .anyMatch(player -> player.getName().equals(name));
    }


    @Override
    public void addChips(String name, long count) {
        final Optional<Pair<String, Player>> optionalPlayer = getPlayerByName(name);
        if (optionalPlayer.isPresent()) {
            final Pair<String, Player> playerPair = optionalPlayer.get();
            final Player player = playerPair.getRight();
            player.addChips(count);
        }
    }

    @Override
    public void addChips(String name) {
        addChips(name, 5000L);
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
    public Player getActivePlayerInGame(String game){
        return getGameByName(game).getRoundSettings()
                .getPlayers()
                .stream()
                .filter(Player::isActive)
                .findFirst().orElseThrow(()-> new RuntimeException("cannot find active player in game:" + game));
    }

    @Override
    public Game getGameByName(String gameName){
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
