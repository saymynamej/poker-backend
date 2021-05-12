package ru.smn.combination.strategy.generator;

import ru.smn.combination.data.CardSizeData;
import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StraightStrategyGenerator implements GeneratorStrategy {

    @Override
    public Combination generate() {
        final List<CardType> cards = CardType.getAllCardsAsList();
        final Random random = new Random();
        final CardType randomCard = cards.get(random.nextInt(cards.size()));

        final List<CardType> straight = cards.stream()
                .filter(cardType -> cardType.getPower() != randomCard.getPower())
                .filter(cardType -> cardType.getPowerAsInt() - randomCard.getPowerAsInt() < CardSizeData.COMBINATION_SIZE)
                .filter(cardType -> !cardType.getSuitType().equals(randomCard.getSuitType()))
                .limit(4)
                .collect(Collectors.toList());

        straight.add(randomCard);

        return Combination.of(
                CombinationType.STRAIGHT,
                straight
        );
    }
}
