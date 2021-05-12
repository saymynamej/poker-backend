package ru.smn.combination.strategy.generator;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FlushStrategyGenerator implements GeneratorStrategy {

    @Override
    public Combination generate() {
        final List<CardType> cards = CardType.getAllCardsAsList();
        final Random random = new Random();
        final CardType.SuitType[] values = CardType.SuitType.values();
        final CardType.SuitType randomSuit = values[random.nextInt(values.length)];

        final List<CardType> suitCards = cards.stream()
                .filter(cardType -> cardType.getSuitType().equals(randomSuit))
                .filter(cardType -> cardType.getPower().getPowerAsInt() % 2 == 0)
                .collect(Collectors.toList());

        return Combination.of(
                CombinationType.FLUSH,
                suitCards
        );
    }
}
