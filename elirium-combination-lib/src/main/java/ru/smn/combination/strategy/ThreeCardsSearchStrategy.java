package ru.smn.combination.strategy;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.combination.data.CardSizeData.THREE_SIZE;
import static ru.smn.combination.utils.CardUtils.*;

public class ThreeCardsSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        final List<CardType> foundThree = new ArrayList<>();

        for (CardType card : cards) {
            final List<CardType> three = filterByPower(cards, card.getPower());

            if (three.size() == THREE_SIZE) {
                foundThree.addAll(three);
            }
        }
        if (!foundThree.isEmpty()) {
            for (int i = 0; i < 2; i++) {
                final CardType biggerCard = findBiggerCardWithFilter(cards, foundThree.get(0).getPower());
                foundThree.add(biggerCard);
                cards.remove(biggerCard);
            }
            return Combination.of(
                    CombinationType.THREE_CARDS,
                    foundThree.stream()
                            .distinct()
                            .collect(Collectors.toList()),
                    sumCards(foundThree)
            );
        }
        return Combination.empty();

    }
}
