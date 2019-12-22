package ru.sm.poker.service;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;
import ru.sm.poker.model.Combination;
import ru.sm.poker.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckWinnerServiceHoldemTest {

    private final CheckCombinationServiceHoldem checkCombinationService
            = new CheckCombinationServiceHoldem();

    private final CheckWinnerService checkWinnerService
            = new CheckWinnerServiceHoldem(checkCombinationService);

    private final List<CardType> flop = Arrays.asList(CardType.SIX_D,CardType.SEVEN_C,CardType.EIGHT_H);
    private final CardType tern = CardType.A_C;
    private final CardType river = CardType.J_S;

    @Test
    public void testWinner(){

        final List<Player> players = new ArrayList<>();

        final Player winner = Player
                .builder()
                .name("winner")
                .cardTypes(new CardType[]{CardType.NINE_H, CardType.FIVE_H})
                .build();

        players.add(winner);


        final Player looser = Player
                .builder()
                .name("looser")
                .cardTypes(new CardType[]{CardType.SIX_H, CardType.SEVEN_D})
                .build();

        players.add(looser);


        final List<Pair<Player, Combination>> winners =
                checkWinnerService.findWinners(players, flop, tern, river);

        assertEquals(winners.get(0).getKey().getName(), winner.getName());
        assertEquals(winners.get(0).getValue().getCombinationType(), CombinationType.STRAIT);

        assertEquals(winners.get(1).getKey().getName(), looser.getName());
        assertEquals(winners.get(1).getValue().getCombinationType(), CombinationType.TWO_PAIR);
    }
}
