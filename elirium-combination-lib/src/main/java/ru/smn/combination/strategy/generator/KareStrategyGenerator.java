package ru.smn.combination.strategy.generator;

import ru.smn.combination.assistant.PowerAssistant;
import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;
import ru.smn.combination.utils.CardUtils;

import java.util.List;
import java.util.Random;

public class KareStrategyGenerator implements GeneratorStrategy {
    @Override
    public Combination generate() {
        final List<CardType> cards = CardType.getAllCardsAsList();

        final Random random = new Random();

        final CardType randomCardForKare = cards.get(random.nextInt(cards.size()));

        final List<CardType> kare = CardUtils.filterByPower(cards, randomCardForKare.getPower());

        cards.removeAll(kare);

        kare.add(cards.get(random.nextInt(cards.size())));

        final int power = PowerAssistant.calc(kare, CombinationType.KARE);

        return Combination.of(
                CombinationType.KARE,
                kare,
                power
        );
    }
}
