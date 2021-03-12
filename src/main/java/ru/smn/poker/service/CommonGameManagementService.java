package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.holdem.Wait;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.dto.HoldemTableSettings;
import ru.smn.poker.entities.*;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.enums.PlayerType;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommonGameManagementService implements GameManagementService {
    private final Map<Game, ExecutorService> runnableGames;
    private final Map<String, Game> games;
    private final ExecutorService executorForListeners = Executors.newCachedThreadPool();
    private final Map<GameType, GameSettings> mapSettings;
    private final GameService gameService;
    private final OrderService orderService;
    private final PrizeService prizeService;
    private final PasswordEncoder passwordEncoder;
    private final RandomNameService randomNameService;
    private final HandService handService;

    @Override
    public void create(int countOfPlayers, long defaultChipsCount, GameType gameType) {
        final String randomName = randomNameService.getRandomName();
        final List<PlayerEntity> players = IntStream.range(0, countOfPlayers).mapToObj(i -> PlayerEntity.builder()
                .name(String.valueOf(i))
                .enable(true)
                .settings(PlayerSettingsEntity.builder()
                        .timeBank(50000L)
                        .gameName(randomName)
                        .playerType(PlayerType.ORDINARY)
                        .build())
                .password(passwordEncoder.encode(String.valueOf(i)))
                .build()).collect(Collectors.toList());

        final Game game = create(defaultChipsCount, gameType, randomName, players);
        run(game);
    }

    @Override
    public void run(GameEntity gameEntity) {
        final Game generatedGame = convert(gameEntity);
        run(generatedGame);
    }

    @Override
    public void run(Game game) {
        saveInCache(game);
        runnableGames.computeIfAbsent(game, game2 -> {
            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                try {
                    game.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return executorService;
        });
    }

    @Override
    public void create(List<PlayerEntity> players, GameType gameType, long defaultChipsCount) {
        final Game game = create(
                defaultChipsCount,
                gameType,
                randomNameService.getRandomName(),
                players
        );

        run(game);
    }

    @Override
    public void createEmptyGame(GameType gameType) {
        final GameEntity emptyGame = GameEntity.builder()
                .gameType(gameType)
                .name(randomNameService.getRandomName())
                .build();

        final GameEntity gameEntityFromBase = gameService.saveGame(emptyGame);

        run(gameEntityFromBase);
    }

    @Override
    public void addListener(Runnable runnable) {
        executorForListeners.submit(runnable);
    }

    private boolean checkGameName(String gameName) {
        return !games.containsKey(gameName);
    }

    private Game convert(GameEntity gameEntity) {
        final GameSettings gameSettings = mapSettings.get(gameEntity.getGameType());

        gameSettings.setGameName(randomNameService.getRandomName());
        gameSettings.setGameId(gameEntity.getId());

        final Table table = new HoldemTable(
                gameEntity.getTables().get(0).getPlayers(),
                orderService,
                prizeService,
                gameSettings,
                handService
        );

        return new HoldemGame(
                gameSettings,
                table
        );
    }

    private Game create(long defaultChipsCount, GameType gameType, String randomName, List<PlayerEntity> players) {
        final GameEntity gameEntity = GameEntity.builder()
                .gameType(gameType)
                .name(randomName)
                .build();

        final TableEntity tableEntity = TableEntity.builder()
                .players(players)
                .build();

        players.forEach(playerEntity -> {
            playerEntity.setGame(gameEntity);
            playerEntity.setChipsCount(ChipsCountEntity.builder()
                    .game(gameEntity)
                    .count(defaultChipsCount)
                    .build());
        });

        tableEntity.setGameEntity(gameEntity);

        gameEntity.setTables(Collections.singletonList(tableEntity));

        final GameEntity gameEntityFromBase = gameService.saveGame(gameEntity);

        return convert(gameEntityFromBase);
    }


    private void saveInCache(Game game) {
        if (checkGameName(game.getGameName())) {
            this.games.put(game.getGameName(), game);
            return;
        }
        throw new RuntimeException("duplicate game with name: " + game.getGameName());
    }
}
