package ru.sm.poker.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;
import ru.sm.poker.model.Combination;
import ru.sm.poker.model.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckWinnerServiceHoldem implements CheckWinnerService {

    private final CheckCombinationServiceHoldem checkCombinationServiceHoldem;

    @Override
    public List<Pair<Player, Combination>> findWinners(List<Player> players, List<CardType> flop, CardType tern, CardType river) {

        final List<Pair<Player, Combination>> playersComb = new ArrayList<>();

        players.forEach(player -> {
            final List<CardType> playersCards = player.getCardTypesAsList();
            playersCards.addAll(flop);
            playersCards.add(tern);
            playersCards.add(river);

            final Pair<CombinationType, List<CardType>> combination =
                    checkCombinationServiceHoldem.findCombination(playersCards);

            final Combination comboDTO = new Combination(combination.getLeft(), combination.getRight());

            playersComb.add(Pair.of(player, comboDTO));
        });

        playersComb.sort((o1, o2) -> o2.getValue().getCombinationType().getPower() - o1.getValue().getCombinationType().getPower());


        return playersComb;
    }
}
