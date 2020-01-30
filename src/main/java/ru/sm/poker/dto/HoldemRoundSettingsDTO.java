package ru.sm.poker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.StageType;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@ToString
public class HoldemRoundSettingsDTO {
    private final Map<PlayerDTO, List<CountAction>> history;
    private final List<PlayerDTO> playerDTOS;
    private final List<CardType> flop;
    private final CardType tern;
    private final CardType river;
    private final String gameName;
    private final long smallBlindBet;
    private final long bigBlindBet;
    private final PlayerDTO button;
    private final PlayerDTO smallBlind;
    private final PlayerDTO bigBlind;
    private final StageType stageType;
    private Long lastBet;
    private long bank;
    private PlayerDTO activePlayerDTO;
    private boolean isAfk;
}
