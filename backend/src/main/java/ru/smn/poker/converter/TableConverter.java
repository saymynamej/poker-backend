package ru.smn.poker.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.ClassicTable;
import ru.smn.poker.game.Table;
import ru.smn.poker.service.HandIdGenerator;
import ru.smn.poker.service.OrderActionService;
import ru.smn.poker.service.PrizeService;
import ru.smn.poker.service.common.TableService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TableConverter {
    private final Map<GameType, GameSettings> mapSettings;
    private final OrderActionService orderActionService;
    private final PrizeService prizeService;
    private final HandIdGenerator handIdGenerator;
    private final TableService tableService;

    public Table toTable(TableEntity tableEntity) {
        final GameSettings gameSettings = mapSettings.get(tableEntity.getGameType());
        gameSettings.setTableId(tableEntity.getId());
        gameSettings.setTableName(tableEntity.getName());

        return new ClassicTable(
                orderActionService,
                prizeService,
                tableEntity.getPlayers(),
                gameSettings,
                gameSettings.getGameType().produceManager(
                        tableEntity.getPlayers(),
                        gameSettings,
                        handIdGenerator,
                        tableService
                )
        );
    }
}
