package ru.smn.combination.strategy;

import ru.smn.combination.data.CardSizeData;
import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;
import ru.smn.combination.utils.CardUtils;

import java.util.List;
import java.util.stream.Collectors;

public class KareSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        final List<CardType> kareCards = cards.stream()
                .collect(Collectors.groupingBy(CardType::getPowerAsInt))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() == CardSizeData.KARE_SIZE)
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toList());

        if (kareCards.isEmpty()) {
            return Combination.empty();
        }

        final CardType kareCard = kareCards.get(0);
        final CardType biggerCardWithoutKare = CardUtils.findBiggerCardWithFilter(cards, kareCard.getPower());

        kareCards.add(biggerCardWithoutKare);

        return Combination.of(
                CombinationType.KARE,
                kareCards,
                kareCard.getPowerAsInt() + biggerCardWithoutKare.getPowerAsInt()
        );
    }
}
