package ru.smn.poker.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.SimpleTable;
import ru.smn.poker.game.Table;
import ru.smn.poker.service.HandService;
import ru.smn.poker.service.OrderActionService;
import ru.smn.poker.service.PrizeService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TableConverter {
    private final Map<GameType, GameSettings> mapSettings;
    private final OrderActionService orderActionService;
    private final PrizeService prizeService;
    private final HandService handService;

    public Table toTable(TableEntity tableEntity) {
        final GameSettings gameSettings = mapSettings.get(tableEntity.getGameType());
        gameSettings.setTableId(tableEntity.getId());
        gameSettings.setTableName(tableEntity.getName());

        return new SimpleTable(
                orderActionService,
                prizeService,
                tableEntity.getPlayers(),
                gameSettings,
                handService
        );
    }
}
