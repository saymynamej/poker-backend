package ru.smn.combination.strategy.generator;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ThreeStrategyGenerator implements GeneratorStrategy {

    @Override
    public Combination generate() {
        final List<CardType> cards = CardType.getAllCardsAsList();
        final Random random = new Random();
        final int randomIndex = random.nextInt(cards.size());
        final CardType randomCard = cards.get(randomIndex);

        final List<CardType> combination = cards.stream()
                .filter(cardType -> cardType.getPower().equals(randomCard.getPower()))
                .limit(3)
                .collect(Collectors.toList());

        final CardType highCard_1 = cards.stream()
                .filter(cardType -> cardType.getPowerAsInt() != randomCard.getPowerAsInt())
                .findAny().orElseThrow();

        combination.add(highCard_1);

        final CardType highCard_2 = cards.stream()
                .filter(cardType -> cardType.getPowerAsInt() != randomCard.getPowerAsInt())
                .filter(cardType -> cardType.getPowerAsInt() != highCard_1.getPowerAsInt())
                .findAny().orElseThrow();

        combination.add(highCard_2);

        return Combination.of(
                CombinationType.THREE_CARDS,
                combination
        );
    }
}
