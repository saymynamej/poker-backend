package ru.smn.combination.utils;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.PowerType;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.smn.combination.data.CardSizeData.COMBINATION_SIZE;

public class CardUtils {

    public static Optional<PowerType> findPowerOfCardWithFilter(List<CardType> cards, Predicate<Map.Entry<PowerType, Long>> predicate) {
        return cards.stream()
                .collect(Collectors.groupingBy(CardType::getPower, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(predicate)
                .sorted(Map.Entry.comparingByKey())
                .limit(1)
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public static boolean containsAllCardsForFlushRoyal(List<CardType> cardTypes) {
        final Optional<CardType> ace = findCard(cardTypes, PowerType.A_POWER);
        final Optional<CardType> king = findCard(cardTypes, PowerType.K_POWER);
        final Optional<CardType> queen = findCard(cardTypes, PowerType.Q_POWER);
        final Optional<CardType> jack = findCard(cardTypes, PowerType.J_POWER);
        final Optional<CardType> ten = findCard(cardTypes, PowerType.TEN_POWER);

        return ace.isPresent() && king.isPresent() && queen.isPresent() && jack.isPresent() && ten.isPresent();
    }

    public static List<CardType> checkStraitWithAce(List<CardType> cards) {
        final List<CardType> distinctList = sortCardsByAsc(removeCardsWithSamePower(cards));

        final Optional<CardType> ace = findCard(distinctList, PowerType.A_POWER);
        final Optional<CardType> two = findCard(distinctList, PowerType.TWO_POWER);
        final Optional<CardType> three = findCard(distinctList, PowerType.THREE_POWER);
        final Optional<CardType> four = findCard(distinctList, PowerType.FOUR_POWER);
        final Optional<CardType> five = findCard(distinctList, PowerType.FIVE_POWER);

        if (ace.isPresent() && two.isPresent() && three.isPresent() && four.isPresent() && five.isPresent()) {
            return Arrays.asList(ace.get(), two.get(), three.get(), four.get(), five.get());
        }

        return Collections.emptyList();
    }

    private static Optional<CardType> findCard(List<CardType> distinctList, PowerType powerType) {
        return distinctList.stream()
                .filter(cardType -> cardType.getPowerAsInt() == powerType.getPowerAsInt())
                .findAny();
    }

    public static boolean isStrait(List<CardType> cards) {
        if (cards.size() != COMBINATION_SIZE) {
            throw new RuntimeException("card size to check straight must be " + COMBINATION_SIZE);
        }

        final List<CardType> cardTypes = sortCardsByDesc(cards);

        int i = cardTypes.get(0).getPowerAsInt();

        for (int m = 1; m < cardTypes.size(); m++) {
            if (i - cardTypes.get(m).getPowerAsInt() != 1) {
                return false;
            }
            i = cardTypes.get(m).getPowerAsInt();
        }
        return true;
    }

    public static List<CardType> removeCardsWithSamePower(List<CardType> cards) {
        return cards.stream()
                .filter(StreamUtils.distinctByKey(CardType::getPowerAsInt))
                .collect(Collectors.toList());
    }

    public static CardType findBiggerCardWithFilter(List<CardType> cardTypes, PowerType filter) {
        return cardTypes.stream()
                .filter(cardType -> cardType.getPower() != (filter))
                .max(Comparator.comparingInt(CardType::getPowerAsInt))
                .orElseThrow(() -> new RuntimeException("cannot find min card"));
    }

    public static List<CardType> filterByPower(List<CardType> cards, PowerType powerFilter) {
        return cards.stream()
                .filter(cardTypeN -> cardTypeN.getPower() == powerFilter)
                .collect(Collectors.toList());
    }

    public static CardType findBiggerCard(List<CardType> cardTypes) {
        return cardTypes.stream()
                .max(Comparator.comparingInt(CardType::getPowerAsInt))
                .orElseThrow(() -> new RuntimeException("cannot find max card"));
    }

    public static int sumCards(List<CardType> cards) {
        return cards.stream().map(CardType::getPowerAsInt)
                .reduce(Integer::sum)
                .orElseThrow(() -> new RuntimeException("cannot calculate sum"));
    }

    public static CardType findSmallerCard(List<CardType> cardTypes) {
        return cardTypes.stream()
                .min(Comparator.comparingInt(CardType::getPowerAsInt))
                .orElseThrow(() -> new RuntimeException("cannot find min card"));
    }

    public static List<CardType> cutFlush(List<CardType> cards) {
        if (cards.size() > COMBINATION_SIZE) {
            cards.remove(findSmallerCard(cards));
            cutFlush(cards);
        }
        return cards;
    }

    public static List<CardType> findFlushBySuit(List<CardType> cardTypes, CardType.SuitType suitType) {
        final List<CardType> cards = cardTypes.stream()
                .filter(cardType -> cardType.getSuitType().equals(suitType))
                .collect(Collectors.toList());

        if (cards.size() >= COMBINATION_SIZE) {
            if (cards.size() > COMBINATION_SIZE) {
                return cutFlush(cards);
            }
            return cards;
        }
        return Collections.emptyList();
    }

    public static List<CardType> sortCardsByDesc(List<CardType> cards) {
        return cards.stream()
                .sorted(ComparatorUtils.desc(CardType::getPowerAsInt))
                .collect(Collectors.toList());
    }

    public static List<CardType> sortCardsByAsc(List<CardType> cards) {
        return cards.stream()
                .sorted(ComparatorUtils.asc(CardType::getPowerAsInt))
                .collect(Collectors.toList());
    }
}
