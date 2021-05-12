package ru.smn.combination.strategy.search;

import ru.smn.combination.assistant.SearchAssistant;
import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.List;

import static ru.smn.combination.utils.CardUtils.containsAllCardsForFlushRoyal;
import static ru.smn.combination.utils.CardUtils.sortCardsByDesc;

public class FlushRoyalSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        final Combination flush = SearchAssistant.find(CombinationType.FLUSH, cards);
        if (!flush.isEmpty() && containsAllCardsForFlushRoyal(flush.getCards())) {
            final CombinationType flushRoyal = CombinationType.FLUSH_ROYAL;
            return Combination.of(flushRoyal, sortCardsByDesc(flush.getCards()));
        }
        return Combination.empty();
    }
}
