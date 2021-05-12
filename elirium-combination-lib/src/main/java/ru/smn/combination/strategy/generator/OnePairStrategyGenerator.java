package ru.smn.combination.strategy.generator;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;
import ru.smn.combination.utils.CardUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

class OnePairStrategyGenerator implements GeneratorStrategy {

    @Override
    public Combination generate() {
        final List<CardType> cards = CardType.getAllCardsAsList();
        final Random random = new Random();
        final CardType randomCard = cards.get(random.nextInt(cards.size()));

        final List<CardType> pair = cards.stream()
                .filter(cardType -> cardType.getPower().equals(randomCard.getPower()))
                .limit(2)
                .collect(Collectors.toList());

        CardUtils.removeCardWithSamePower(cards, pair.get(0).getPower());

        for (int i = 0; i < 3; i++) {
            final CardType randomHighCard = cards.get(random.nextInt(cards.size()));
            pair.add(randomHighCard);
            CardUtils.removeCardWithSamePower(cards, randomHighCard.getPower());
        }

        return Combination.of(
                CombinationType.ONE_PAIR,
                pair
        );
    }
}
