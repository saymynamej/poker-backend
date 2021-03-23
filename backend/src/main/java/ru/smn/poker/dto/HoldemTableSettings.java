package ru.smn.poker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.smn.poker.action.Action;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.TableSettings;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@ToString
public class HoldemTableSettings implements TableSettings {
    private final Map<PlayerEntity, List<Action>> stageHistory;
    private final Map<PlayerEntity, List<Action>> fullHistory;
    private final List<PlayerEntity> players;
    private final List<CardType> flop;
    private final CardType tern;
    private final CardType river;
    private final String gameName;
    private final long smallBlindBet;
    private final long gameId;
    private final long bigBlindBet;
    private final PlayerEntity button;
    private final PlayerEntity smallBlind;
    private final PlayerEntity bigBlind;
    private final StageType stageType;
    private Long lastBet;
    private long bank;
    private Long handId;
    private PlayerEntity activePlayer;
    private boolean isAfk;
    private boolean isFinished;

    public boolean isNotFirstMoveOnBigBlind(){
        return bigBlind.getSettings().get(0).isNotFirstMoveOnBigBlind();
    }

}
