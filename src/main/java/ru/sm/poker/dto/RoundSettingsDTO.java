package ru.sm.poker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.model.Player;

import java.util.List;

@Builder
@Getter
@Setter
public class RoundSettingsDTO {
    private final List<Player> players;
    private final List<CardType> flop;
    private final CardType tern;
    private final CardType river;
    private final String gameName;
    private final Player button;
    private final Player smallBlind;
    private final Player bigBlind;

    private Long lastBet;
    private long bank;
    private Player activePlayer;
}