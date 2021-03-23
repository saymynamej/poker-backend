package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.PlayerSettingsEntity;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.enums.PlayerType;
import ru.smn.poker.game.HoldemTable;
import ru.smn.poker.game.Table;
import ru.smn.poker.repository.PlayerRepository;
import ru.smn.poker.service.GameManagementService;
import ru.smn.poker.service.OrderActionService;
import ru.smn.poker.service.PrizeService;
import ru.smn.poker.service.RandomNameService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleGameManagementService implements GameManagementService {
    private final Map<String, Table> games;
    private final Map<GameType, GameSettings> mapSettings;
    private final OrderActionService orderActionService;
    private final PrizeService prizeService;
    private final HandService handService;
    private final RandomNameService randomNameService;
    private final PasswordEncoder passwordEncoder;
    private final PlayerRepository playerRepository;
    private final TableService tableService;
    private final ExecutorService tables = Executors.newCachedThreadPool();

    public void createTable(int countOfPlayers, GameType gameType) {
        final String randomName = randomNameService.getRandomName();

        final List<PlayerEntity> players = IntStream.range(0, countOfPlayers).mapToObj(i -> PlayerEntity.builder()
                .name(String.valueOf(i))
                .enable(true)
                .settings(singletonList(PlayerSettingsEntity.builder()
                        .timeBank(50000L)
                        .chipsCount(ChipsCountEntity.builder()
                                .count(5000L)
                                .build())
                        .gameName(randomName)
                        .playerType(PlayerType.ORDINARY)
                        .build()))
                .password(passwordEncoder.encode(String.valueOf(i)))
                .build())
                .collect(Collectors.toList());

        createTable(players, gameType, randomName);
    }

    public void createTable(List<PlayerEntity> players, GameType gameType, String gameName) {
        TableEntity tableEntity = TableEntity.builder()
                .players(players)
                .build();

        tableService.save(tableEntity);

//        final GameSettings gameSettings = mapSettings.get(gameType);
//
//        gameSettings.setGameName(gameName);
//        gameSettings.setTableId(5L);
//
//        final Table table = new HoldemTable(
//                players,
//                orderActionService,
//                prizeService,
//                gameSettings,
//                handService
//        );
//
//        run(table);
    }

    public void run(Table table) {
        if (games.containsKey(table.getGameName())) {
            throw new RuntimeException("table with name: " + table.getGameName() + " already exist");
        }
        games.put(table.getGameName(), table);
        tables.submit(() -> {
            try {
                table.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
