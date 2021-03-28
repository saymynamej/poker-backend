package ru.smn.poker.dto;

import lombok.*;
import ru.smn.poker.action.Action;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.TableSettings;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoldemTableSettings implements TableSettings {
    private Map<PlayerEntity, List<Action>> stageHistory;
    private Map<PlayerEntity, List<Action>> fullHistory;
    private List<PlayerEntity> players;
    private List<CardType> flop;
    private CardType tern;
    private CardType river;
    private String tableName;
    private long smallBlindBet;
    private long tableId;
    private long bigBlindBet;
    private PlayerEntity button;
    private PlayerEntity smallBlind;
    private PlayerEntity bigBlind;
    private StageType stageType;
    private Long lastBet;
    private long bank;
    private Long handId;
    private PlayerEntity activePlayer;
    private boolean isAfk;
    private boolean isFinished;

    public boolean isNotFirstMoveOnBigBlind(){
        return bigBlind.getSettings().get(0).isNotFirstMoveOnBigBlind();
    }

    @Override
    public TableSettings copy(List<PlayerEntity> players) {
        final HoldemTableSettings tableSettings = new HoldemTableSettings();
        tableSettings.setStageHistory(stageHistory);
        tableSettings.setFullHistory(fullHistory);
        tableSettings.setPlayers(players);
        tableSettings.setFlop(flop);
        tableSettings.setTern(tern);
        tableSettings.setRiver(river);
        tableSettings.setTableName(tableName);
        tableSettings.setSmallBlindBet(smallBlindBet);
        tableSettings.setTableId(tableId);
        tableSettings.setBigBlindBet(bigBlindBet);
        tableSettings.setButton(button);
        tableSettings.setSmallBlind(smallBlind);
        tableSettings.setBigBlind(bigBlind);
        tableSettings.setStageType(stageType);
        tableSettings.setLastBet(lastBet);
        tableSettings.setBank(bank);
        tableSettings.setHandId(handId);
        tableSettings.setActivePlayer(activePlayer);
        tableSettings.setAfk(isAfk);
        tableSettings.setFinished(isFinished);
        return tableSettings;
    }

    @Override
    public TableSettings copy() {
        return copy(players);
    }
}
