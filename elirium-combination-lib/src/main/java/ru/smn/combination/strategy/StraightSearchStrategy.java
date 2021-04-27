package ru.smn.combination.strategy;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.combination.utils.CardUtils.*;

public class StraightSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        if (isStrait(cards)) {
            final List<CardType> sortedCards = sortCardsByDesc(removeCardsWithSamePower(cards));

            if (sortedCards.size() == 5) {
                return Combination.of(
                        CombinationType.STRAIGHT,
                        sortedCards,
                        findBiggerCard(sortedCards).getPowerAsInt()
                );
            }

            while (true) {
                final List<CardType> firstFiveCards = sortedCards.stream()
                        .limit(5)
                        .collect(Collectors.toList());

                if (isStrait(firstFiveCards)) {
                    return Combination.of(
                            CombinationType.STRAIGHT,
                            firstFiveCards,
                            findBiggerCard(firstFiveCards).getPowerAsInt()
                    );
                }
                sortedCards.remove(0);
            }
        }
        final List<CardType> straitWithAce = checkStraitWithAce(cards);

        if (!straitWithAce.isEmpty()) {
            return Combination.of(
                    CombinationType.STRAIGHT,
                    straitWithAce,
                    5 // because 5 is bigger card for this combination type
            );
        }
        return Combination.empty();
    }
}
