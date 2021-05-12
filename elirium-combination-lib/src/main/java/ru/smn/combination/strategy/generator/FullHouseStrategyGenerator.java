package ru.smn.combination.strategy.generator;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FullHouseStrategyGenerator implements GeneratorStrategy {

    @Override
    public Combination generate() {
        final List<CardType> cards = CardType.getAllCardsAsList();
        final Random random = new Random();
        final int randomIndexForThree = random.nextInt(cards.size());
        final CardType randomCardForThree = cards.get(randomIndexForThree);

        final List<CardType> threeCards = cards.stream()
                .filter(cardType -> cardType.getPower().equals(randomCardForThree.getPower()))
                .limit(3)
                .collect(Collectors.toList());

        final List<CardType> cardsWithoutThreeType = cards.stream()
                .filter(cardType -> !cardType.getPower().equals(randomCardForThree.getPower()))
                .collect(Collectors.toList());

        final int randomIndexForTwo = random.nextInt(cardsWithoutThreeType.size());
        final CardType randomCardForTwo = cardsWithoutThreeType.get(randomIndexForTwo);

        final List<CardType> twoCards = cardsWithoutThreeType.stream()
                .filter(cardType -> cardType.getPower().equals(randomCardForTwo.getPower()))
                .limit(2)
                .collect(Collectors.toList());

        return Combination.of(
                CombinationType.FULL_HOUSE,
                Stream.concat(
                        threeCards.stream(),
                        twoCards.stream()
                ).collect(Collectors.toList())
        );
    }
}
