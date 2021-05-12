package ru.smn.combination.strategy.search;

import ru.smn.combination.data.CardSizeData;
import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;
import ru.smn.combination.utils.CardUtils;

import java.util.List;

import static ru.smn.combination.utils.CardUtils.*;

public class StraightSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        final List<CardType> sortedCards = sortCardsByDesc(removeCardsWithSamePower(cards));
        final CombinationType straight = CombinationType.STRAIGHT;

        for (int i = 0; i < sortedCards.size() % 4; i++) {
            final List<CardType> cardTypes = sortedCards.subList(i, CardSizeData.COMBINATION_SIZE + i);
            if (CardUtils.isStrait(cardTypes)) {
                return Combination.of(straight, cardTypes);
            }
        }

        final List<CardType> straightWithAce = checkStraitWithAce(cards);

        if (!straightWithAce.isEmpty()) {
            return Combination.of(straight, straightWithAce);
        }

        return Combination.empty();
    }
}
