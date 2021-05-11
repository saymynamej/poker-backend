package ru.smn.combination.strategy.search;

import ru.smn.combination.assistant.PowerAssistant;
import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;
import ru.smn.combination.assistant.SearchAssistant;

import java.util.List;

import static ru.smn.combination.utils.CardUtils.*;

public class FlushRoyalSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        final Combination flush = SearchAssistant.find(CombinationType.FLUSH, cards);
        if (!flush.isEmpty() && containsAllCardsForFlushRoyal(flush.getCards())) {
            final CombinationType flushRoyal = CombinationType.FLUSH_ROYAL;
            final int power = PowerAssistant.calc(flush.getCards(), flushRoyal);

            return Combination.of(
                    flushRoyal,
                    sortCardsByDesc(flush.getCards()),
                    power
            );
        }
        return Combination.empty();
    }
}
