package ru.smn.combination.strategy;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.ArrayList;
import java.util.List;

import static ru.smn.combination.data.CardSizeData.COMBINATION_SIZE;
import static ru.smn.combination.data.CardSizeData.PAIR_SIZE;
import static ru.smn.combination.utils.CardUtils.*;

public class TwoPairSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        final int size = cards.size();
        final List<CardType> copyList = new ArrayList<>(cards);
        final List<CardType> firstPair = new ArrayList<>();
        final List<CardType> secondPair = new ArrayList<>();
        final List<CardType> combination = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            final CardType biggerCard = findBiggerCard(cards);
            final List<CardType> pair = filterByPower(cards, biggerCard.getPower());

            if (pair.size() == PAIR_SIZE && firstPair.isEmpty()) {
                firstPair.addAll(pair);
                copyList.removeAll(pair);
            } else if (pair.size() == PAIR_SIZE) {
                secondPair.addAll(pair);
                copyList.removeAll(pair);
            }
            if (!firstPair.isEmpty() && !secondPair.isEmpty()) {
                break;
            }
            cards.remove(biggerCard);
        }
        combination.addAll(secondPair);
        combination.addAll(firstPair);
        combination.add(findBiggerCard(copyList));

        if (combination.size() == COMBINATION_SIZE) {
            return Combination.of(
                    CombinationType.TWO_PAIR,
                    combination,
                    sumCards(combination)
            );
        }
        return Combination.empty();
    }
}
