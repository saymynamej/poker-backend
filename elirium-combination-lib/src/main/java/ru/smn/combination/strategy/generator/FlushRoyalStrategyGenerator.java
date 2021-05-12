package ru.smn.combination.strategy.generator;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;
import ru.smn.combination.data.PowerType;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

class FlushRoyalStrategyGenerator implements GeneratorStrategy {

    @Override
    public Combination generate() {
        final List<CardType> cards = CardType.getAllCardsAsList();

        final Random random = new Random();

        final CardType.SuitType[] values = CardType.SuitType.values();

        final CardType.SuitType suitForFlushRoyal = values[random.nextInt(values.length)];

        final List<CardType> flushRoyal = cards.stream()
                .filter(cardType -> cardType.getSuitType().equals(suitForFlushRoyal))
                .takeWhile(cardType -> cardType.getPower().getPowerAsInt() >= PowerType.TEN_POWER.getPowerAsInt())
                .collect(Collectors.toList());

        return Combination.of(
                CombinationType.FLUSH_ROYAL,
                flushRoyal
        );
    }
}
