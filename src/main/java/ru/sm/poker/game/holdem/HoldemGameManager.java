package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ErrorType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.model.Player;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.sm.poker.enums.ErrorType.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class HoldemGameManager implements GameManager {

    private final Map<String, Game> games;
    private final Queue<Player> players;

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
    public boolean createNewGame(String name, Game game) {
        if (checkGameName(name)) {
            games.put(name, game);
            return true;
        }
        return false;

    }

    @Override
    public void removePlayer(Player player) {
        games.forEach((name, game) -> {
            final List<Player> players = game.getRoundSettings().getPlayers();
            players.remove(player);
        });
    }

    @Override
    public ErrorType joinInQueue(Player player) {
        if (players.add(player)) {
            player.setStateType(StateType.IN_GAME);
            return SUCCESS_JOIN_IN_QUEUE;
        }
        return PLAYER_ALREADY_EXIST;
    }

    @Override
    public ErrorType joinInGame(String gameName, Player player) throws RuntimeException {
        if (checkGameName(gameName)) {
            return GAME_NOT_FOUND;
        }
        final Game game = games.get(gameName);

        if (game.getRoundSettings().getPlayers().size() == game.getMaxPlayersSize()) {
            return SETTINGS_NOT_FOUND;
        }
        player.setStateType(StateType.IN_GAME);
        game.addPlayer(player);
        return SUCCESS_JOIN_IN_QUEUE;
    }

    @Override
    public void reload(String playerName) {
        final Optional<Pair<String, Player>> player = getPlayerByName(playerName);
        if (player.isPresent()) {
            final Pair<String, Player> playerPair = player.get();
            final Game game = getGames().get(playerPair.getLeft());
            game.reload();
        }
    }

    @Override
    public Map<String, Game> getGames() {
        return games;
    }


    private boolean checkGameName(String gameName) {
        return !games.containsKey(gameName);
    }

    @PostConstruct
    public void init() {
        joinInQueue(Player.builder()
                .name("1")
                .chipsCount(5000)
                .timeBank(60L)
                .build());
        joinInQueue(Player.builder()
                .name("2")
                .chipsCount(5000)
                .timeBank(60L)
                .build());
        joinInQueue(Player.builder()
                .name("3")
                .chipsCount(5000)
                .timeBank(60L)
                .build());
    }
}
