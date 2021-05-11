package ru.smn.combination.strategy.generator;

import ru.smn.combination.assistant.PowerAssistant;
import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.combination.data.CardSizeData.COMBINATION_SIZE;

public class HighCardStrategyGenerator implements GeneratorStrategy {

    @Override
    public Combination generate() {
        final List<CardType> cards = CardType.getAllCardsAsList();

        Collections.shuffle(cards);

        final List<CardType> combinationCards = cards.stream()
                .limit(COMBINATION_SIZE)
                .collect(Collectors.toList());

        final CombinationType highCard = CombinationType.HIGH_CARD;

        return Combination.of(
                highCard,
                combinationCards,
                PowerAssistant.calc(combinationCards, highCard)
        );
    }
}
