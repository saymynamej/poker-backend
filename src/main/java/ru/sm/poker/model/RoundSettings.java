package ru.sm.poker.model;

import lombok.Builder;
import lombok.Getter;
import ru.sm.poker.enums.CardType;

import java.util.List;

@Builder
@Getter
public class RoundSettings {

    private final List<Player> players;
    private final List<CardType> flop;
    private final CardType tern;
    private final CardType river;
    private final long bank;
    private final Player button;
    private final Player smallBlind;
    private final Player bigBlind;
    private final Player activePlayer;

}
