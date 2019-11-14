package ru.sm.poker.service;

import org.springframework.stereotype.Service;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.Combination;
import ru.sm.poker.enums.PowerType;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CheckCombinationServiceHoldem implements CheckCombinationService {

    private final static int ONE_POWER = 1;
    private final static int COMBINATION_SIZE = 5;
    private final static int FULL_COMBINATION_SIZE = 7;

    private final static int KARE_SIZE = 4;
    private final static int PAIR_SIZE = 2;
    private final static int THREE_SIZE = 3;

    private final static int NEXT_CARD_INDEX = 1;
    private final static int NUMBER_OF_COMPARISONS = 4;

    @Override
    public Map<Combination, List<CardType>> findCombination(List<CardType> cards) {

        if (cards.size() != FULL_COMBINATION_SIZE) {
            throw new RuntimeException("cards size must be 7");
        }

        final Map<Combination, List<CardType>> combinations = new HashMap<>();

        //FLUSH ROYAL
        final List<CardType> flushRoyal = findFlushRoyal(new ArrayList<>(cards));
        if (!flushRoyal.isEmpty() && checkSize(flushRoyal) && checkDuplicate(flushRoyal)) {
            combinations.put(Combination.FLUSH_ROYAL, flushRoyal);
            return combinations;
        }
        //STRAIGHT FLUSH

        final List<CardType> straitFlush = findStraitFlush(new ArrayList<>(cards));
        if (!straitFlush.isEmpty() && checkSize(straitFlush) && checkDuplicate(straitFlush)) {
            combinations.put(Combination.STRAIT_FLUSH, straitFlush);
            return combinations;
        }

        //KARE
        final List<CardType> kare = findKare(new ArrayList<>(cards));
        if (!kare.isEmpty() && checkSize(kare) && checkDuplicate(kare)) {
            combinations.put(Combination.KARE, kare);
            return combinations;
        }

        //FULL-HOUSE

        final List<CardType> fullHouse = findFullHouse(new ArrayList<>(cards));
        if (!fullHouse.isEmpty() && checkSize(fullHouse) && checkDuplicate(fullHouse)) {
            combinations.put(Combination.FULL_HOUSE, fullHouse);
            return combinations;
        }

        //FLUSH
        final List<CardType> flush = findFlush(new ArrayList<>(cards));
        if (!flush.isEmpty() && checkSize(flush) && checkDuplicate(flush)) {
            combinations.put(Combination.FLUSH, flush);
            return combinations;
        }

        //STRAIT
        final List<CardType> strait = findStrait(new ArrayList<>(cards));
        if (!strait.isEmpty() && checkSize(strait) && checkDuplicate(strait)) {
            combinations.put(Combination.STRAIT, strait);
            return combinations;
        }

        //THREE
        final List<CardType> three = findThree(new ArrayList<>(cards));
        if (!three.isEmpty() && checkSize(three) && checkDuplicate(three)) {
            combinations.put(Combination.THREE, three);
            return combinations;
        }

        //TWO PAIR

        final List<CardType> twoPair = findTwoPair(new ArrayList<>(cards));
        if (!twoPair.isEmpty() && checkSize(twoPair) && checkDuplicate(twoPair)) {
            combinations.put(Combination.TWO_PAIR, twoPair);
            return combinations;
        }

        //PAIR

        final List<CardType> pair = findPair(new ArrayList<>(cards));

        if (!pair.isEmpty() && checkSize(pair) && checkDuplicate(pair)) {
            combinations.put(Combination.PAIR, pair);
            return combinations;
        }

        //HIGH CARD
        final List<CardType> highCards = findHighCard(new ArrayList<>(cards));

        if (!highCards.isEmpty() && checkSize(highCards) && checkDuplicate(highCards)) {
            combinations.put(Combination.HIGH_CARD, highCards);
            return combinations;
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
        List<CardType> copyList = new ArrayList<>(cards);


        //DELETE CARDS WITH SAME POWER
        for (CardType card : copyList) {
            List<CardType> samePowerCards = filterByPower(copyList, card.getPower());
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





    private List<CardType> sortByPowerDesc(List<CardType> cards){
        return cards.stream()
                .sorted(Comparator.comparingInt(CardType::getPowerAsInt).reversed())
                .collect(Collectors.toList());
    }


    private List<CardType> sortByPowerAsc(List<CardType> cards){
        return cards.stream()
                .sorted(Comparator.comparingInt(CardType::getPowerAsInt))
                .collect(Collectors.toList());
    }

    private boolean checkStrait(List<CardType> cards) {
        final List<CardType> strait = sortByPowerAsc(cards);


        int repeat = 0;

        for (int i = 0; i < strait.size(); i++) {
            if (i + NEXT_CARD_INDEX != strait.size()) {

                boolean isNext = strait.get(i).getPower().getPowerAsInt() + ONE_POWER == strait.get(i + NEXT_CARD_INDEX).getPower().getPowerAsInt();
                boolean isNext2 = strait.get(i).getPower().getPowerAsInt() + ONE_POWER - strait.get(i + NEXT_CARD_INDEX).getPower().getPowerAsInt() == 1;
                if (isNext) {
                    repeat++;
                } else {
                    if (!isNext2) {
                        repeat = 0;
                    }
                }
            }
        }
        return repeat >= NUMBER_OF_COMPARISONS;
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
        final List<CardType> cards = cardTypes
                .stream()
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

    private boolean checkDuplicate(List<CardType> cards) {
        if (cards
                .stream()
                .distinct()
                .collect(Collectors.toList())
                .size() == COMBINATION_SIZE) {
            return true;
        }
        throw new RuntimeException("Global warning, in process checking duplicates cards");
    }
}
