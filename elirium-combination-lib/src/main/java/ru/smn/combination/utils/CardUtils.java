package ru.smn.combination.utils;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.PowerType;

import java.util.*;
import java.util.stream.Collectors;

import static ru.smn.combination.data.CardSizeData.COMBINATION_SIZE;

public class CardUtils {

    public static boolean containsAllCardsForFlushRoyal(List<CardType> cardTypes) {
        final List<CardType> flushRoyal = cardTypes.stream()
                .filter(cardType -> cardType.getPower() == PowerType.A_POWER
                        || cardType.getPower() == PowerType.K_POWER
                        || cardType.getPower() == PowerType.Q_POWER
                        || cardType.getPower() == PowerType.J_POWER
                        || cardType.getPower() == PowerType.TEN_POWER).collect(Collectors.toList()
                );

        return flushRoyal.size() == COMBINATION_SIZE;
    }

    public static List<CardType> checkStraitWithAce(List<CardType> cards) {
        final List<CardType> distinctList = sortCardsByAsc(removeCardsWithSamePower(cards));

        final Optional<CardType> ace = distinctList.stream()
                .filter(cardType -> cardType.getPowerAsInt() == PowerType.A_POWER.getPowerAsInt())
                .findAny();

        final Optional<CardType> two = distinctList.stream()
                .filter(cardType -> cardType.getPowerAsInt() == PowerType.TWO_POWER.getPowerAsInt())
                .findAny();

        final Optional<CardType> three = distinctList.stream()
                .filter(cardType -> cardType.getPowerAsInt() == PowerType.THREE_POWER.getPowerAsInt())
                .findAny();

        final Optional<CardType> four = distinctList.stream()
                .filter(cardType -> cardType.getPowerAsInt() == PowerType.FOUR_POWER.getPowerAsInt())
                .findAny();

        final Optional<CardType> five = distinctList.stream()
                .filter(cardType -> cardType.getPowerAsInt() == PowerType.FIVE_POWER.getPowerAsInt())
                .findAny();

        if (ace.isPresent() && two.isPresent() && three.isPresent() && four.isPresent() && five.isPresent()) {
            return Arrays.asList(ace.get(), two.get(), three.get(), four.get(), five.get());
        }

        return Collections.emptyList();
    }

    public static boolean isStrait(List<CardType> cards) {
        final List<CardType> distinctList = sortCardsByAsc(removeCardsWithSamePower(cards));
        int i = 0;
        for (CardType cardFromFirstLoop : distinctList) {
            CardType equalsCard = cardFromFirstLoop;
            for (CardType cardFromSecondLoop : distinctList) {
                if (cardFromFirstLoop.equals(cardFromSecondLoop)) {
                    continue;
                }
                if (equalsCard.getPowerAsInt() - cardFromSecondLoop.getPowerAsInt() == -1) {
                    i++;
                } else {
                    if (i < 4) {
                        i = 0;
                    }
                }
                equalsCard = cardFromSecondLoop;
            }
            if (i >= 4) {
                return true;
            } else {
                i = 0;
            }
        }
        return false;
    }

    public static List<CardType> removeCardsWithSamePower(List<CardType> cards) {
        final List<CardType> distinctList = new ArrayList<>();

        for (CardType card : cards) {
            if (distinctList.isEmpty()) {
                distinctList.add(card);
                continue;
            }
            if (distinctList.stream().noneMatch(cardType -> cardType.getPowerAsInt() == card.getPowerAsInt())) {
                distinctList.add(card);
            }
        }
        return distinctList;
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
