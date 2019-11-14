package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.model.Player;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class HoldemManager implements GameManager {
    private final Map<String, Game> games;
    private final List<Player> players;


    public List<Game> getPlayerByName() {
        return (List<Game>) games
                .values();
    }

    @Override
    public Map<String, Player> getPlayerByName(String name) {
        return games
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
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
    public void removePlayer(Player player) {
        games.forEach((name, game) -> {
            final List<Player> players = game.getRoundSettings().getPlayers();
            players.remove(player);
        });
    }

    @Override
    public void addPlayer(Player player) {
        players.add(player);
    }

    @PostConstruct
    public void init() {
        players.add(Player.builder().name("Oleg").chipsCount(5000).build());
        players.add(Player.builder().name("Oleg1").chipsCount(5000).build());
        players.add(Player.builder().name("Oleg2").chipsCount(5000).build());
    }
}
