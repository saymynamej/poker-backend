package ru.smn.combination.strategy.generator;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TwoPairStrategyGenerator implements GeneratorStrategy {

    @Override
    public Combination generate() {
        final List<CardType> cards = CardType.getAllCardsAsList();
        final Random random = new Random();
        final CardType firstRandomCard = cards.get(random.nextInt(cards.size()));

        final List<CardType> firstPair = cards.stream()
                .filter(cardType -> cardType.getPower().equals(firstRandomCard.getPower()))
                .limit(2)
                .collect(Collectors.toList());

        cards.removeAll(
                cards.stream()
                        .filter(cardType -> cardType.getPower().equals(firstRandomCard.getPower()))
                        .collect(Collectors.toList())
        );

        final CardType secondRandomCard = cards.get(random.nextInt(cards.size()));

        final List<CardType> secondPair = cards.stream()
                .filter(cardType -> cardType.getPower().equals(secondRandomCard.getPower()))
                .limit(2)
                .collect(Collectors.toList());

        cards.removeAll(
                cards.stream()
                        .filter(cardType -> cardType.getPower().equals(secondRandomCard.getPower()))
                        .collect(Collectors.toList())
        );

        final List<CardType> combination = Stream.concat(
                firstPair.stream(),
                secondPair.stream()
        ).collect(Collectors.toList());

        combination.add(cards.get(random.nextInt(cards.size())));

        return Combination.of(
                CombinationType.TWO_PAIR,
                combination
        );
    }
}
