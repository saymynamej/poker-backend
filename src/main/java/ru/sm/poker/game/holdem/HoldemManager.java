package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.BroadCastService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class HoldemManager implements GameManager {

    private final Map<String, Game> games;
    private final Queue<Player> players;
    private final BroadCastService broadCastService;

    @Override
    public Optional<Pair<String, Player>> getPlayerByName(String name) {
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

        return Optional.empty();
    }


    @Override
    public boolean playerExistByName(String gameName, String name) {
        return games
                .get(gameName)
                .getRoundSettings()
                .getPlayers()
                .stream()
                .anyMatch(exist -> exist.getName().equals(name));
    }

    @Override
    public void removePlayer(String gameName, Player player) {
        final Game game = games.get(gameName);
        if (game != null) {
            game.removePlayer(player);
            log.info("Player removed :" + player.getName());
            return;
        }
        throw new RuntimeException(String.format("could not found player in game =%s, player=%s", gameName, player.getName()));
    }

    @Override
    public boolean createNewGame(String name, Game game) {
        if (checkGameName(name)) {
            return false;
        } else {
            games.put(name, game);
            return true;
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
    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            broadCastService.sendToAll(player.getName() + " is ready");
        } else {
            broadCastService.sendErrorToUser(player.getName(), " you have registration yet");
            log.info("player already exist");
        }
    }

    @Override
    public Map<String, Game> getAllGames() {
        return games;
    }


    private boolean checkGameName(String gameName) {
        return games.containsKey(gameName);
    }

    @PostConstruct
    public void init() {
        players.add(Player.builder().name("1").chipsCount(5000).build());
        players.add(Player.builder().name("2").chipsCount(5000).build());
        players.add(Player.builder().name("3").chipsCount(5000).build());
    }
}
