package ru.smn.combination.strategy.search;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.List;

import static ru.smn.combination.utils.CardUtils.findFlushBySuit;
import static ru.smn.combination.utils.CardUtils.sortCardsByDesc;

public class FlushSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        for (CardType.SuitType suitType : CardType.SuitType.values()) {
            final List<CardType> flushBySuit = findFlushBySuit(cards, suitType);
            if (!flushBySuit.isEmpty()) {
                final CombinationType flush = CombinationType.FLUSH;
                return Combination.of(
                        flush,
                        sortCardsByDesc(flushBySuit)
                );
            }
        }
        return Combination.empty();
    }
}
