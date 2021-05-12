package ru.smn.combination.strategy.generator;

import ru.smn.combination.data.Combination;

public class OnePairStrategyGenerator implements GeneratorStrategy {

    @Override
    public Combination generate() {
//        final List<CardType> cards = CardType.getAllCardsAsList();
//
//        final Random random = new Random();
//
//        final CardType firstCardForPair = cards.get(random.nextInt(cards.size()));
//
//        cards.remove(firstCardForPair);
//
//        final CardType secondCardForPair = cards.stream()
//                .filter(card -> card.getPower().equals(firstCardForPair.getPower()))
//                .findAny()
//                .orElseThrow();
//
//        final List<CardType> combination = Stream.of(firstCardForPair, secondCardForPair)
//                .collect(Collectors.toList());
//
//        for (int i = 0; i < 3; i++) {
//            final CardType cardType = cards.get(random.nextInt(cards.size()));
//            combination.add(cardType);
//            cards.remove(cardType);
//        }
//
//        final int power = PowerAssistant.calc(combination, CombinationType.ONE_PAIR);
//
//        return Combination.of(
//                CombinationType.ONE_PAIR,
//                combination,
//                power
//        );

        return Combination.empty();
    }
}
