package ru.smn.poker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.smn.poker.action.Action;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.enums.StateType;
import ru.smn.poker.util.PlayerUtil;
import ru.smn.poker.util.StreamUtil;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@ToString
public class HoldemRoundSettings implements RoundSettings {
    private final Map<Player, List<ru.smn.poker.action.Action>> stageHistory;
    private final Map<Player, List<Action>> fullHistory;
    private final List<Player> players;
    private final List<CardType> flop;
    private final CardType tern;
    private final CardType river;
    private final String gameName;
    private final long smallBlindBet;
    private final long gameId;
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
