package ru.sm.poker.service;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;
import ru.sm.poker.model.Combination;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.holdem.CombinationServiceHoldem;
import ru.sm.poker.service.holdem.WinnerServiceHoldem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WinnerServiceHoldemTest {

    private final CombinationServiceHoldem checkCombinationService
            = new CombinationServiceHoldem();

    private final WinnerService winnerService
            = new WinnerServiceHoldem(checkCombinationService);

    private final List<CardType> flop = Arrays.asList(CardType.SIX_D, CardType.SEVEN_C, CardType.EIGHT_H);
    private final CardType tern = CardType.A_C;
    private final CardType river = CardType.J_S;

    @Test
    public void testWinner() {

        final List<Player> players = new ArrayList<>();

        final Player winner = Player
                .builder()
                .name("winner")
                .cards(Arrays.asList(CardType.NINE_H, CardType.FIVE_H))
                .build();

        players.add(winner);

        final Player looser = Player
                .builder()
                .name("looser")
                .cards(Arrays.asList(CardType.SIX_H, CardType.SEVEN_D))
                .build();

        players.add(looser);

        final Player looser2 = Player
                .builder()
                .name("looser2")
                .cards(Arrays.asList(CardType.NINE_C, CardType.A_D))
                .build();

        players.add(looser2);

        final Player looser3 = Player
                .builder()
                .name("looser3")
                .cards(Arrays.asList(CardType.TWO_C, CardType.TWO_D))
                .build();

        players.add(looser3);


        final List<Pair<Player, Combination>> winners =
                winnerService.findWinners(players, flop, tern, river);

        assertEquals(winners.get(0).getKey().getName(), winner.getName());
        assertEquals(winners.get(0).getValue().getCombinationType(), CombinationType.STRAIT);

        assertEquals(winners.get(1).getKey().getName(), looser.getName());
        assertEquals(winners.get(1).getValue().getCombinationType(), CombinationType.TWO_PAIR);

        assertEquals(winners.get(2).getKey().getName(), looser2.getName());
        assertEquals(winners.get(2).getValue().getCombinationType(), CombinationType.PAIR);

        assertEquals(winners.get(3).getKey().getName(), looser3.getName());
        assertEquals(winners.get(3).getValue().getCombinationType(), CombinationType.PAIR);
    }
}
