package ru.smn.combination.strategy;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.combination.data.CardSizeData.THREE_SIZE;
import static ru.smn.combination.utils.CardUtils.filterByPower;

public class FullHouseSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        final List<CardType> foundThree = new ArrayList<>();
        final List<CardType> foundTwo = new ArrayList<>();
        final List<CardType> copySorted = new ArrayList<>(cards).stream()
                .sorted(Comparator.comparingInt(CardType::getPowerAsInt).reversed())
                .collect(Collectors.toList());

        for (CardType card : copySorted) {
            final List<CardType> three = filterByPower(cards, card.getPower());
            if (three.size() == THREE_SIZE) {
                foundThree.addAll(three);
                cards.removeAll(three);
                break;
            }
        }

        for (CardType card : copySorted) {
            final List<CardType> two = filterByPower(cards, card.getPower());
            if (two.size() == 2) {
                foundTwo.addAll(two);
                cards.removeAll(two);
                break;
            }
            if (two.size() > 2) {
                foundTwo.add(two.get(0));
                foundTwo.add(two.get(1));
                break;
            }
        }

        if (!foundTwo.isEmpty() && !foundThree.isEmpty()) {
            final List<CardType> fullHouse = new ArrayList<>();
            fullHouse.addAll(foundTwo);
            fullHouse.addAll(foundThree);
            return Combination.of(
                    CombinationType.FULL_HOUSE,
                    fullHouse,
                    foundThree.get(0).getPowerAsInt() + foundTwo.get(0).getPowerAsInt()
            );
        }

        return Combination.empty();
    }
}
