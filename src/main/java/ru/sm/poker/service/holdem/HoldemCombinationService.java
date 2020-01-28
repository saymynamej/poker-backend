package ru.sm.poker.service.holdem;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;
import ru.sm.poker.enums.PowerType;
import ru.sm.poker.service.CombinationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoldemCombinationService implements CombinationService {

    private final static int ONE_POWER = 1;
    private final static int COMBINATION_SIZE = 5;
    private final static int FULL_COMBINATION_SIZE = 7;

    private final static int KARE_SIZE = 4;
    private final static int PAIR_SIZE = 2;
    private final static int THREE_SIZE = 3;

    private final static int NUMBER_OF_COMPARISONS = 4;


    @Override
    public Pair<CombinationType, List<CardType>> findCombination(List<CardType> cards) {

        if (cards.size() != FULL_COMBINATION_SIZE) {
            throw new RuntimeException("cards size must be 7");
        }

        //FLUSH ROYAL
        final List<CardType> flushRoyal = findFlushRoyal(new ArrayList<>(cards));
        if (!flushRoyal.isEmpty() && checkSize(flushRoyal) && checkDuplicate(flushRoyal)) {
            return ImmutablePair.of(CombinationType.FLUSH_ROYAL, flushRoyal);
        }
        //STRAIGHT FLUSH

        final List<CardType> straitFlush = findStraitFlush(new ArrayList<>(cards));
        if (!straitFlush.isEmpty() && checkSize(straitFlush) && checkDuplicate(straitFlush)) {
            return ImmutablePair.of(CombinationType.STRAIT_FLUSH, straitFlush);
        }

        //KARE
        final List<CardType> kare = findKare(new ArrayList<>(cards));
        if (!kare.isEmpty() && checkSize(kare) && checkDuplicate(kare)) {
            return ImmutablePair.of(CombinationType.KARE, kare);
        }

        //FULL-HOUSE

        final List<CardType> fullHouse = findFullHouse(new ArrayList<>(cards));
        if (!fullHouse.isEmpty() && checkSize(fullHouse) && checkDuplicate(fullHouse)) {
            return ImmutablePair.of(CombinationType.FULL_HOUSE, fullHouse);
        }

        //FLUSH
        final List<CardType> flush = findFlush(new ArrayList<>(cards));
        if (!flush.isEmpty() && checkSize(flush) && checkDuplicate(flush)) {
            return ImmutablePair.of(CombinationType.FLUSH, flush);
        }

        //STRAIT
        final List<CardType> strait = findStrait(new ArrayList<>(cards));
        if (!strait.isEmpty() && checkSize(strait) && checkDuplicate(strait)) {
            return ImmutablePair.of(CombinationType.STRAIT, strait);
        }

        //THREE
        final List<CardType> three = findThree(new ArrayList<>(cards));
        if (!three.isEmpty() && checkSize(three) && checkDuplicate(three)) {
            return ImmutablePair.of(CombinationType.THREE, three);
        }

        //TWO PAIR

        final List<CardType> twoPair = findTwoPair(new ArrayList<>(cards));
        if (!twoPair.isEmpty() && checkSize(twoPair) && checkDuplicate(twoPair)) {
            return ImmutablePair.of(CombinationType.TWO_PAIR, twoPair);
        }

        //PAIR

        final List<CardType> pair = findPair(new ArrayList<>(cards));

        if (!pair.isEmpty() && checkSize(pair) && checkDuplicate(pair)) {
            return ImmutablePair.of(CombinationType.PAIR, pair);
        }

        //HIGH CARD
        final List<CardType> highCards = findHighCard(new ArrayList<>(cards));

        if (!highCards.isEmpty() && checkSize(highCards) && checkDuplicate(highCards)) {
            return ImmutablePair.of(CombinationType.HIGH_CARD, highCards);
        }

        throw new RuntimeException("global error, could not found poker combination!");
    }


    private List<CardType> findPair(List<CardType> cards) {
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
        }

        return foundPair;

    }

    private List<CardType> findFullHouse(List<CardType> cards) {

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
            return fullHouse.stream()
                    .distinct()
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }


    private List<CardType> findTwoPair(List<CardType> cards) {
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
            } else if (pair.size() == PAIR_SIZE && secondPair.isEmpty()) {
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

        if (combination.size() != COMBINATION_SIZE) {
            return Collections.emptyList();
        }

        return combination;
    }


    private List<CardType> findThree(List<CardType> cards) {
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
            return foundThree
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    private List<CardType> findHighCard(List<CardType> cards) {
        final List<CardType> highCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final CardType biggerCard = findBiggerCard(cards);
            highCards.add(biggerCard);
            cards.remove(biggerCard);
        }
        return highCards;
    }


    private List<CardType> findStraitFlush(List<CardType> cardTypes) {
        final List<CardType> flush = findFlush(cardTypes);
        if (checkStrait(flush)) {
            return flush;
        }
        return Collections.emptyList();
    }

    private List<CardType> findKare(List<CardType> cards) {
        final List<CardType> foundKare = new ArrayList<>();

        for (CardType card : cards) {
            final List<CardType> kare = filterByPower(cards, card.getPower());
            if (kare.size() == KARE_SIZE) {
                foundKare.addAll(kare);
                break;
            }
        }

        if (foundKare.isEmpty()) {
            return Collections.emptyList();
        }

        final List<CardType> distinctKare = foundKare
                .stream()
                .distinct()
                .collect(Collectors.toList());

        distinctKare.add(findBiggerCardWithFilter(cards, foundKare.get(0).getPower()));

        return distinctKare;

    }


    private List<CardType> filterByPower(List<CardType> cards, PowerType powerFilter) {
        return cards
                .stream()
                .filter(cardTypeN -> cardTypeN.getPower() == powerFilter)
                .collect(Collectors.toList());
    }

    private List<CardType> findFlushRoyal(List<CardType> cardTypes) {
        final List<CardType> flush = findFlush(cardTypes);
        if (checkFlushRoyal(flush)) {
            return flush;
        }
        return Collections.emptyList();
    }


    private boolean checkFlushRoyal(List<CardType> cardTypes) {
        final List<CardType> flushRoyal = cardTypes.stream().filter(cardType -> cardType.getPower() == PowerType.A_POWER
                || cardType.getPower() == PowerType.K_POWER
                || cardType.getPower() == PowerType.Q_POWER
                || cardType.getPower() == PowerType.J_POWER
                || cardType.getPower() == PowerType.TEN_POWER).collect(Collectors.toList());

        return flushRoyal.size() == COMBINATION_SIZE;
    }

    private List<CardType> findStrait(List<CardType> cardTypes) {
        if (checkStrait(cardTypes)) {
            cutStraight(cardTypes);
            return cardTypes.stream()
                    .sorted(Comparator.comparingInt(CardType::getPowerAsInt).reversed())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    private List<CardType> cutStraight(List<CardType> cards) {
        final List<CardType> copyList = new ArrayList<>(cards);

        //DELETE CARDS WITH SAME POWER
        for (CardType card : copyList) {
            final List<CardType> samePowerCards = filterByPower(copyList, card.getPower());
            if (samePowerCards.size() > 1) {
                cards.removeAll(samePowerCards);
                cards.add(samePowerCards.get(0));
            }
        }

        if (cards.size() > COMBINATION_SIZE) {
            cards.remove(findSmallerCard(cards));
            cutFlush(cards);
        }
        return sortByPowerDesc(cards);
    }


    private List<CardType> sortByPowerDesc(List<CardType> cards) {
        return cards.stream()
                .sorted(Comparator.comparingInt(CardType::getPowerAsInt).reversed())
                .collect(Collectors.toList());
    }


    private List<CardType> sortByPowerAsc(List<CardType> cards) {
        return cards.stream()
                .sorted(Comparator.comparingInt(CardType::getPowerAsInt))
                .collect(Collectors.toList());
    }

    private boolean checkStrait(List<CardType> cards) {

        final List<CardType> distinctList = removeCardsWithSamePower(cards);
        int i = 0;

        for (CardType cardFromFirstLoop : distinctList) {
            for (CardType cardFromSecondLoop : distinctList) {
                if (cardFromFirstLoop.equals(cardFromSecondLoop)) {
                    continue;
                }
                final int powerAsInt = cardFromFirstLoop.getPowerAsInt();
                final int powerAsInt1 = cardFromSecondLoop.getPowerAsInt();
                if (powerAsInt1 - powerAsInt == ONE_POWER) {
                    i++;
                }
            }
            if (i >= NUMBER_OF_COMPARISONS) {
                return true;
            }
        }
        return false;
    }


    private List<CardType> findFlush(List<CardType> cardTypes) {

        final List<CardType> spadeFlush = findFlushBySuit(cardTypes, CardType.SuitType.SPADE);

        if (!spadeFlush.isEmpty()) {
            return spadeFlush;
        }

        final List<CardType> heartFlush = findFlushBySuit(cardTypes, CardType.SuitType.HEART);

        if (!heartFlush.isEmpty()) {
            return heartFlush;
        }

        final List<CardType> clubFlush = findFlushBySuit(cardTypes, CardType.SuitType.CLUB);

        if (!clubFlush.isEmpty()) {
            return clubFlush;
        }

        final List<CardType> diamondFlush = findFlushBySuit(cardTypes, CardType.SuitType.DIAMOND);

        if (!diamondFlush.isEmpty()) {
            return diamondFlush;
        }

        return Collections.emptyList();
    }

    private List<CardType> findFlushBySuit(List<CardType> cardTypes, CardType.SuitType suitType) {
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


    private List<CardType> cutFlush(List<CardType> cards) {
        if (cards.size() > COMBINATION_SIZE) {
            cards.remove(findSmallerCard(cards));
            cutFlush(cards);
        }
        return cards;
    }

    private CardType findSmallerCard(List<CardType> cardTypes) {
        return cardTypes
                .stream()
                .min(Comparator.comparingInt(CardType::getPowerAsInt))
                .orElseThrow(() -> new RuntimeException("cannot find min card"));
    }

    private CardType findBiggerCardWithFilter(List<CardType> cardTypes, PowerType filter) {
        return cardTypes
                .stream()
                .filter(cardType -> cardType.getPower() != (filter))
                .max(Comparator.comparingInt(CardType::getPowerAsInt))
                .orElseThrow(() -> new RuntimeException("cannot find min card"));
    }

    private CardType findBiggerCard(List<CardType> cardTypes) {
        return cardTypes
                .stream()
                .max(Comparator.comparingInt(CardType::getPowerAsInt))
                .orElseThrow(() -> new RuntimeException("cannot find max card"));
    }

    private boolean checkSize(List<CardType> cards) {
        if (cards.size() == COMBINATION_SIZE) {
            return true;
        }
        throw new RuntimeException(String.format("Global warning, in process checking size must be: %s, but : %s", COMBINATION_SIZE, cards.size()));
    }

    private List<CardType> removeCardsWithSamePower(List<CardType> cards) {
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

    private boolean checkDuplicate(List<CardType> cards) {
        if (cards.stream()
                .distinct()
                .count() == COMBINATION_SIZE) {
            return true;
        }
        throw new RuntimeException("Global warning, in process checking duplicates cards");
    }
}
