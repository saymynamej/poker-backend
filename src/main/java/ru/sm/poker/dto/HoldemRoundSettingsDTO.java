package ru.sm.poker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.sm.poker.action.Action;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.StageType;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@ToString
public class HoldemRoundSettingsDTO {
    private final Map<Player, List<Action>> stageHistory;
    private final Map<Player, List<Action>> fullHistory;
    private final List<Player> players;
    private final List<CardType> flop;
    private final CardType tern;
    private final CardType river;
    private final String gameName;
    private final long smallBlindBet;
    private final long bigBlindBet;
    private final Player button;
    private final Player smallBlind;
    private final Player bigBlind;
    private final StageType stageType;
    private Long lastBet;
    private long bank;
    private Player activePlayer;
    private boolean isAfk;
}
