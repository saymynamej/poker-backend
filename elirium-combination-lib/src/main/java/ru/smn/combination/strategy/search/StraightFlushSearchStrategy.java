package ru.smn.combination.strategy.search;

import ru.smn.combination.assistant.PowerAssistant;
import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;
import ru.smn.combination.assistant.SearchAssistant;

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
                final int power = PowerAssistant.calc(straitFlushWithAce.getCards(), CombinationType.STRAIGHT_FLUSH);

                return Combination.of(
                        CombinationType.STRAIGHT_FLUSH,
                        sortCardsByDesc(straitFlushWithAce.getCards()),
                        power
                );
            }
        }
        return Combination.empty();
    }
}
