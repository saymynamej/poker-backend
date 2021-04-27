package ru.smn.combination.strategy;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.combination.data.CardSizeData.PAIR_SIZE;
import static ru.smn.combination.utils.CardUtils.findBiggerCard;
import static ru.smn.combination.utils.CardUtils.sumCards;

public class OnePairSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        final int size = cards.size();
        final List<CardType> foundPair = new ArrayList<>();
        final List<CardType> copyList = new ArrayList<>(cards);

        for (int i = 0; i < size; i++) {
            final CardType biggerCard = findBiggerCard(cards);

            final List<CardType> pair = cards.stream()
                    .filter(cardType -> cardType.getPower() == biggerCard.getPower())
                    .collect(Collectors.toList());
            cards.remove(biggerCard);
            if (pair.size() == PAIR_SIZE) {
                foundPair.addAll(pair);
                break;
            }
        }
        if (foundPair.size() == PAIR_SIZE) {
            copyList.removeAll(foundPair);
            for (int i = 0; i < 3; i++) {
                final CardType biggerCard = findBiggerCard(copyList);
                foundPair.add(biggerCard);
                copyList.remove(biggerCard);
            }
            return Combination.of(
                    CombinationType.ONE_PAIR,
                    foundPair,
                    sumCards(foundPair)
            );
        }
        return Combination.empty();
    }
}
