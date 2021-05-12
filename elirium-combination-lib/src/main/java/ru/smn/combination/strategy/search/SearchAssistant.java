package ru.smn.combination.strategy.search;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.smn.combination.utils.CardUtils.sortByDesc;

public class SearchAssistant {
    private final static Map<CombinationType, SearchStrategy> searchStrategies = new HashMap<>();

    static {
        searchStrategies.put(CombinationType.HIGH_CARD, new HighCardSearchStrategy());
        searchStrategies.put(CombinationType.ONE_PAIR, new OnePairSearchStrategy());
        searchStrategies.put(CombinationType.TWO_PAIR, new TwoPairSearchStrategy());
        searchStrategies.put(CombinationType.THREE_CARDS, new ThreeCardsSearchStrategy());
        searchStrategies.put(CombinationType.STRAIGHT, new StraightSearchStrategy());
        searchStrategies.put(CombinationType.FLUSH, new FlushSearchStrategy());
        searchStrategies.put(CombinationType.FULL_HOUSE, new FullHouseSearchStrategy());
        searchStrategies.put(CombinationType.KARE, new KareSearchStrategy());
        searchStrategies.put(CombinationType.STRAIGHT_FLUSH, new StraightFlushSearchStrategy());
        searchStrategies.put(CombinationType.FLUSH_ROYAL, new FlushRoyalSearchStrategy());
    }

    public static Combination find(CombinationType combinationType, List<CardType> cards) {
        final SearchStrategy searchStrategy = searchStrategies.get(combinationType);
        final Combination combination = searchStrategy.find(cards);
        if (combination.isEmpty()) {
            return combination;
        }
        return Combination.of(
                combination.getCombinationType(),
                sortByDesc(combination.getCards()),
                combination.getPower()
        );
    }
}
