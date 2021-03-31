package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.SimpleTable;
import ru.smn.poker.game.Table;
import ru.smn.poker.generator.PlayerGenerator;
import ru.smn.poker.service.GameManagementService;
import ru.smn.poker.service.OrderActionService;
import ru.smn.poker.service.PrizeService;
import ru.smn.poker.service.RandomNameService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleGameManagementService implements GameManagementService {
    private final Map<String, Table> games;
    private final Map<GameType, GameSettings> mapSettings;
    private final OrderActionService orderActionService;
    private final PrizeService prizeService;
    private final SimpleHandService handService;
    private final RandomNameService randomNameService;
    private final TableService tableService;
    private final PlayerGenerator playerGenerator;
    private final ExecutorService tables = Executors.newCachedThreadPool();

    public void createTable(int countOfPlayers, GameType gameType) {
        final String randomName = randomNameService.getRandomName();

        createTable(
                playerGenerator.generate(countOfPlayers, randomName),
                randomName,
                gameType
        );
    }

    public void createTable(List<PlayerEntity> players, String tableName, GameType gameType) {
        final TableEntity tableEntity = TableEntity.builder()
                .players(players)
                .gameType(gameType)
                .name(tableName)
                .build();
        setTableForPlayers(players, tableEntity);
        tableService.save(tableEntity);

        final Table table = toTable(tableEntity);

        run(table);
    }

    private void setTableForPlayers(List<PlayerEntity> players, TableEntity tableEntity) {
        players.forEach(playerEntity -> playerEntity.setTables(Collections.singletonList(tableEntity)));
    }

    public Table toTable(TableEntity tableEntity) {
        final GameSettings gameSettings = mapSettings.get(tableEntity.getGameType());
        gameSettings.setTableId(tableEntity.getId());
        gameSettings.setTableName(tableEntity.getName());

        return new SimpleTable(
                orderActionService,
                prizeService,
                handService,
                tableEntity.getPlayers(),
                gameSettings
        );
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
