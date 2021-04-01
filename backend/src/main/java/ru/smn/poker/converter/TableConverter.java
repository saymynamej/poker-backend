package ru.smn.poker.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.dto.ClassicTableSettings;
import ru.smn.poker.entities.HandEntity;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.ClassicTable;
import ru.smn.poker.game.Table;
import ru.smn.poker.game.TableSettingsManagerFactory;
import ru.smn.poker.service.HandIdGenerator;
import ru.smn.poker.service.OrderActionService;
import ru.smn.poker.service.PrizeService;
import ru.smn.poker.service.common.TableService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TableConverter {
    private final Map<GameType, GameSettings> mapSettings;
    private final OrderActionService orderActionService;
    private final PrizeService prizeService;
    private final HandIdGenerator handIdGenerator;
    private final TableService tableService;


    public Table restore(TableEntity tableEntity) {
        final GameSettings gameSettings = mapSettings.get(tableEntity.getGameType());
        gameSettings.setTableId(tableEntity.getId());
        gameSettings.setTableName(tableEntity.getName());

        final HandEntity lastHand = tableEntity.getLastHand();

        final ClassicTableSettings classicTableSettings = ClassicTableSettings.builder()
                .activePlayer(lastHand.getActivePlayer())
                .bank(lastHand.getBank())
                .bigBlind(lastHand.getBigBlind())
                .smallBlind(lastHand.getSmallBlind())
                .bigBlindBet(lastHand.getBigBlindBet())
                .smallBlindBet(lastHand.getSmallBlindBet())
                .button(lastHand.getButton())
                .flop(List.of(lastHand.getF1(), lastHand.getF2(), lastHand.getF3()))
                .players(tableEntity.getPlayers())
                .handId(lastHand.getId())
                .tableId(tableEntity.getId())
                .tableName(tableEntity.getName())
                .lastBet(lastHand.getLastBet())
                .river(lastHand.getRiver())
                .tern(lastHand.getTern())
                .build();

        return new ClassicTable(
                orderActionService,
                prizeService,
                tableEntity.getPlayers(),
                gameSettings,
                TableSettingsManagerFactory.restoreManagerHoldemHU(
                        tableEntity.getPlayers(),
                        gameSettings,
                        handIdGenerator,
                        tableService,
                        classicTableSettings
                )
        );
    }

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
