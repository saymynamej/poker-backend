package ru.smn.combination.strategy;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;
import ru.smn.combination.data.SearchAssistant;

import java.util.List;

import static ru.smn.combination.utils.CardUtils.*;

public class StraightFlushSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        final Combination flush = SearchAssistant.find(CombinationType.FLUSH, cards);

        if (!flush.isEmpty() && isStrait(flush.getCards())) {
            return Combination.of(
                    CombinationType.STRAIGHT_FLUSH,
                    sortCardsByDesc(flush.getCards()),
                    findBiggerCard(flush.getCards()).getPowerAsInt()
            );
        }
        final List<CardType> straitWithAce = checkStraitWithAce(cards);
        if (!straitWithAce.isEmpty()) {
            final Combination straitFlushWithAce = SearchAssistant.find(CombinationType.FLUSH, straitWithAce);
            if (!straitFlushWithAce.isEmpty()) {
                return Combination.of(
                        CombinationType.STRAIGHT_FLUSH,
                        sortCardsByDesc(straitFlushWithAce.getCards()),
                        5 // because 5 is bigger card for this combination type
                );
            }
        }
        return Combination.empty();
    }
}
