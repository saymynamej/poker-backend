package ru.sm.poker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@ToString
public class RoundSettingsDTO {
    private final Map<Player, List<CountAction>> history = new HashMap<>();
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
