package ru.smn.combination.strategy;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.combination.data.CardSizeData.COMBINATION_SIZE;
import static ru.smn.combination.utils.CardUtils.sortCardsByDesc;
import static ru.smn.combination.utils.CardUtils.sumCards;

public class HighCardSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        final List<CardType> highCardCombination = sortCardsByDesc(cards)
                .stream()
                .limit(COMBINATION_SIZE)
                .collect(Collectors.toList());

        return Combination.of(CombinationType.HIGH_CARD, highCardCombination, sumCards(highCardCombination));
    }
}
