package ru.smn.poker.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.holdem.Wait;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.dto.ClassicTableSettings;
import ru.smn.poker.entities.ActionEntity;
import ru.smn.poker.entities.HandEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.ClassicTable;
import ru.smn.poker.game.Table;
import ru.smn.poker.game.TableSettingsManagerFactory;
import ru.smn.poker.service.HandIdGenerator;
import ru.smn.poker.service.OrderActionService;
import ru.smn.poker.service.PrizeService;
import ru.smn.poker.service.common.TableService;
import ru.smn.poker.util.HistoryUtil;

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

        final List<ActionEntity> actions = lastHand.getActions();

        final Map<PlayerEntity, List<Action>> fullHistory = HistoryUtil.getFullHistory(actions);

        final Map<PlayerEntity, List<Action>> stageHistory = HistoryUtil.getStageHistory(actions, lastHand.getStageType());

        setActionsForPlayers(tableEntity, stageHistory);

        final ClassicTableSettings classicTableSettings = ClassicTableSettings.builder()
                .activePlayer(lastHand.getActivePlayer())
                .bank(lastHand.getBank())
                .bigBlind(lastHand.getBigBlind())
                .smallBlind(lastHand.getSmallBlind())
                .fullHistory(fullHistory)
                .stageHistory(stageHistory)
                .bigBlindBet(lastHand.getBigBlindBet())
                .smallBlindBet(lastHand.getSmallBlindBet())
                .button(lastHand.getButton())
                .players(tableEntity.getPlayers())
                .handId(lastHand.getId())
                .tableId(tableEntity.getId())
                .tableName(tableEntity.getName())
                .lastBet(lastHand.getLastBet())
                .river(lastHand.getRiver())
                .tern(lastHand.getTern())
                .build();

        if (lastHand.getF1() != null) {
            classicTableSettings.setFlop(List.of(lastHand.getF1(), lastHand.getF2(), lastHand.getF3()));
        }

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

    private void setActionsForPlayers(TableEntity tableEntity, Map<PlayerEntity, List<Action>> stageHistory) {
        if (stageHistory.isEmpty()) {
            tableEntity.getPlayers().forEach(playerEntity -> playerEntity.getTableSettings().setAction(new Wait()));
        } else {
            tableEntity.getPlayers().forEach(playerEntity -> {
                final List<Action> actions = stageHistory.get(playerEntity);
                if (actions == null || actions.isEmpty()) {
                    playerEntity.getTableSettings().setAction(new Wait());
                } else {
                    playerEntity.getTableSettings().setAction(actions.get(actions.size() - 1));
                }
            });
        }
    }

}
