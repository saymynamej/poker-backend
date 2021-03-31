package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smn.poker.converter.TableConverter;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.Table;
import ru.smn.poker.generator.PlayerGenerator;
import ru.smn.poker.service.GameManagementService;
import ru.smn.poker.service.RandomNameService;
import ru.smn.poker.service.TableRunner;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleGameTableCreator implements GameManagementService {
    private final RandomNameService randomNameService;
    private final TableService tableService;
    private final PlayerGenerator playerGenerator;
    private final TableConverter tableConverter;
    private final TableRunner tableRunner;

    public void createTable(int countOfPlayers, GameType gameType) {
        final String randomName = randomNameService.getRandomName();

        createTable(
                playerGenerator.generate(countOfPlayers, randomName),
                randomName,
                gameType
        );
    }

    public void createTable(
            List<PlayerEntity> players,
            String tableName,
            GameType gameType
    ) {
        final TableEntity tableEntity = TableEntity.builder()
                .players(players)
                .gameType(gameType)
                .name(tableName)
                .build();

        players.forEach(playerEntity -> playerEntity.setTables(Collections.singletonList(tableEntity)));

        tableService.save(tableEntity);

        final Table table = tableConverter.toTable(tableEntity);

        tableRunner.run(table);
    }

}
