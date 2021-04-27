package ru.smn.combination.strategy;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;
import ru.smn.combination.data.SearchAssistant;

import java.util.List;

import static ru.smn.combination.utils.CardUtils.*;

public class FlushRoyalSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        final Combination flush = SearchAssistant.find(CombinationType.FLUSH, cards);
        if (!flush.isEmpty() && containsAllCardsForFlushRoyal(flush.getCards())) {
            return Combination.of(
                    CombinationType.FLUSH_ROYAL,
                    sortCardsByDesc(flush.getCards()),
                    findBiggerCard(flush.getCards()).getPowerAsInt()
            );
        }
        return Combination.empty();
    }
}
