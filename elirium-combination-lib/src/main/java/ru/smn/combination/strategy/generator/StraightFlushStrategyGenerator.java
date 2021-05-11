package ru.smn.combination.strategy.generator;

import ru.smn.combination.assistant.PowerAssistant;
import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;
import ru.smn.combination.data.PowerType;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StraightFlushStrategyGenerator implements GeneratorStrategy {
    @Override
    public Combination generate() {
        final List<CardType> cards = CardType.getAllCardsAsList();
        final Random random = new Random();
        final CardType cardType = cards.get(random.nextInt(cards.size()));

        if (cardType.getPower() == PowerType.A_POWER) {
            return generateWithAce(cards, cardType);
        }

        final List<CardType> suitCards = cards.stream()
                .filter(e -> e.getSuitType().equals(cardType.getSuitType()))
                .collect(Collectors.toList());

        final int start = random.nextInt(7) + 1;

        final List<CardType> straightFlush = suitCards.subList(start, start + 5);

        final int power = PowerAssistant.calc(straightFlush, CombinationType.STRAIGHT_FLUSH);

        return Combination.of(
                CombinationType.STRAIGHT_FLUSH,
                straightFlush,
                power
        );
    }

    private Combination generateWithAce(List<CardType> cards, CardType cardType) {
        final List<CardType> straightFlushWithAce = cards.stream()
                .filter(filter -> filter.getSuitType().equals(cardType.getSuitType()) &&
                        (filter.getPower() == PowerType.FIVE_POWER ||
                                filter.getPower() == PowerType.FOUR_POWER ||
                                filter.getPower() == PowerType.THREE_POWER ||
                                filter.getPower() == PowerType.TWO_POWER)
                )
                .collect(Collectors.toList());

        straightFlushWithAce.add(cardType);

        final int power = PowerAssistant.calc(straightFlushWithAce, CombinationType.STRAIGHT_FLUSH);

        return Combination.of(
                CombinationType.STRAIGHT_FLUSH,
                straightFlushWithAce,
                power
        );
    }
}
