package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.converter.TableConverter;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.Table;
import ru.smn.poker.generator.PlayerGenerator;
import ru.smn.poker.service.GameTableCreator;
import ru.smn.poker.service.RandomNameService;
import ru.smn.poker.service.TableRunner;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleGameTableCreator implements GameTableCreator {
    private final RandomNameService randomNameService;
    private final TableService tableService;
    private final PlayerGenerator playerGenerator;
    private final TableConverter tableConverter;
    private final TableRunner tableRunner;

    @Override
    public void create(int countOfPlayers, GameType gameType) {
        final String randomName = randomNameService.getRandomName();

        create(
                playerGenerator.generate(countOfPlayers, randomName),
                randomName,
                gameType
        );
    }

    @Override
    public void restore(TableEntity tableEntity) {
        final Table table = tableConverter.restore(tableEntity);
        tableRunner.run(table);
    }

    @Override
    public void create(
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

    @Override
    @Transactional
    public void restoreAll() {
        tableService.findAll().forEach(this::restore);
    }
}
