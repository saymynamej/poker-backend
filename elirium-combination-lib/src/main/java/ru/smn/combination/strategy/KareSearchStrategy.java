package ru.smn.combination.strategy;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;
import ru.smn.combination.data.PowerType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.combination.data.CardSizeData.KARE_SIZE;
import static ru.smn.combination.utils.CardUtils.filterByPower;
import static ru.smn.combination.utils.CardUtils.findBiggerCardWithFilter;

public class KareSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        final List<CardType> foundKare = new ArrayList<>();

        for (final CardType card : cards) {
            final List<CardType> kare = filterByPower(cards, card.getPower());
            if (kare.size() == KARE_SIZE) {
                foundKare.addAll(kare);
                break;
            }
        }

        if (foundKare.isEmpty()) {
            return Combination.empty();
        }

        final List<CardType> distinctKare = foundKare
                .stream()
                .distinct()
                .collect(Collectors.toList());

        final PowerType powerOfKare = foundKare.get(0).getPower();
        final CardType biggerCardWithFilter = findBiggerCardWithFilter(cards, powerOfKare);

        distinctKare.add(biggerCardWithFilter);

        return Combination.of(
                CombinationType.KARE,
                distinctKare,
                powerOfKare.getPowerAsInt() + biggerCardWithFilter.getPowerAsInt()
        );
    }
}
