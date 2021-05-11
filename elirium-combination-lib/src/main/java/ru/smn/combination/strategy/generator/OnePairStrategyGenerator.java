package ru.smn.combination.strategy.generator;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;

import java.util.List;
import java.util.stream.Collectors;

public class OnePairStrategyGenerator implements GeneratorStrategy {

    @Override
    public Combination generate() {
        final List<CardType> cards = CardType.getAllCardsAsList().stream()
                .limit(3)
                .collect(Collectors.toList());

        final CardType cardType = CardType.getAllCardsAsListWithFilter(cards)
                .stream()
                .limit(1).findFirst().orElseThrow();

        return null;
    }
}
