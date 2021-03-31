package ru.smn.poker.service.classic;

import org.springframework.stereotype.Service;
import ru.smn.poker.dto.Combination;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.enums.CombinationType;
import ru.smn.poker.enums.PowerType;
import ru.smn.poker.service.CombinationService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassicCombinationService implements CombinationService {
    private final static int COMBINATION_SIZE = 5;
    private final static int FULL_COMBINATION_SIZE = 7;
    private final static int KARE_SIZE = 4;
    private final static int PAIR_SIZE = 2;
    private final static int THREE_SIZE = 3;

    @Override
    public Combination findCombination(List<CardType> cards) {
        if (cards.size() != FULL_COMBINATION_SIZE) {
            throw new RuntimeException("cards size must be 7");
        }

        final Combination flushRoyal = findFlushRoyal(new ArrayList<>(cards));
        if (!flushRoyal.isEmpty()) {
            return flushRoyal;
        }

        final Combination straitFlush = findStraitFlush(new ArrayList<>(cards));
        if (!straitFlush.isEmpty()) {
            return straitFlush;
        }

        final Combination kare = findKare(new ArrayList<>(cards));
        if (!kare.isEmpty()) {
            return kare;
        }

        final Combination fullHouse = findFullHouse(new ArrayList<>(cards));
        if (!fullHouse.isEmpty()) {
            return fullHouse;
        }

        final Combination flush = findFlush(new ArrayList<>(cards));
        if (!flush.isEmpty()) {
            return flush;
        }

        final Combination strait = findStrait(new ArrayList<>(cards));
        if (!strait.isEmpty()) {
            return strait;
        }

        final Combination three = findThree(new ArrayList<>(cards));
        if (!three.isEmpty()) {
            return three;
        }

        final Combination twoPair = findTwoPair(new ArrayList<>(cards));
        if (!twoPair.isEmpty()) {
            return twoPair;
        }

        final Combination pair = findPair(new ArrayList<>(cards));
        if (!pair.isEmpty()) {
            return pair;
        }

        final Combination highCards = findHighCard(new ArrayList<>(cards));
        if (!highCards.isEmpty()) {
            return highCards;
        }

        throw new RuntimeException("global error, could not found poker combination!");
    }

    private int sumCards(List<CardType> cards) {
        return cards.stream().map(CardType::getPowerAsInt)
                .reduce(Integer::sum)
                .orElseThrow(() -> new RuntimeException("cannot calculate sum"));
    }

    private Combination findPair(List<CardType> cards) {
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
                    CombinationType.PAIR,
                    foundPair,
                    sumCards(foundPair)
            );
        }
        return Combination.empty();
    }

    private Combination findFullHouse(List<CardType> cards) {
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

    private Combination findTwoPair(List<CardType> cards) {
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

    private Combination findThree(List<CardType> cards) {
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
                    CombinationType.THREE,
                    foundThree.stream()
                            .distinct()
                            .collect(Collectors.toList()),
                    sumCards(foundThree)
            );
        }
        return Combination.empty();

    }

    private Combination findHighCard(List<CardType> cards) {
        final List<CardType> highCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final CardType biggerCard = findBiggerCard(cards);
            highCards.add(biggerCard);
            cards.remove(biggerCard);
        }
        return Combination.of(
                CombinationType.HIGH_CARD,
                highCards,
                sumCards(highCards)
        );
    }

    private Combination findStraitFlush(List<CardType> cardTypes) {
        final Combination flush = findFlush(cardTypes);

        if (!flush.isEmpty() && isStrait(flush.getCards())) {
            return Combination.of(
                    CombinationType.STRAIT_FLUSH,
                    sortByPowerDesc(flush.getCards()),
                    findBiggerCard(flush.getCards()).getPowerAsInt()
            );
        }
        final List<CardType> straitWithAce = checkStraitWithAce(cardTypes);
        if (!straitWithAce.isEmpty()) {
            final Combination straitFlushWithAce = findFlush(straitWithAce);
            if (!straitFlushWithAce.isEmpty()) {
                return Combination.of(
                        CombinationType.STRAIT_FLUSH,
                        sortByPowerDesc(straitFlushWithAce.getCards()),
                        5 // because 5 is bigger card for this combination type
                );
            }
        }
        return Combination.empty();
    }

    private Combination findKare(List<CardType> cards) {
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

    private List<CardType> filterByPower(List<CardType> cards, PowerType powerFilter) {
        return cards.stream()
                .filter(cardTypeN -> cardTypeN.getPower() == powerFilter)
                .collect(Collectors.toList());
    }

    private Combination findFlushRoyal(List<CardType> cardTypes) {
        final Combination flush = findFlush(cardTypes);
        if (!flush.isEmpty() && isFlushRoyal(flush.getCards())) {
            return Combination.of(
                    CombinationType.FLUSH_ROYAL,
                    sortByPowerDesc(flush.getCards()),
                    findBiggerCard(flush.getCards()).getPowerAsInt()
            );
        }
        return Combination.empty();
    }


    private boolean isFlushRoyal(List<CardType> cardTypes) {
        final List<CardType> flushRoyal = cardTypes.stream().filter(cardType -> cardType.getPower() == PowerType.A_POWER
                || cardType.getPower() == PowerType.K_POWER
                || cardType.getPower() == PowerType.Q_POWER
                || cardType.getPower() == PowerType.J_POWER
                || cardType.getPower() == PowerType.TEN_POWER).collect(Collectors.toList());

        return flushRoyal.size() == COMBINATION_SIZE;
    }


    private Combination findStrait(List<CardType> cards) {
        if (isStrait(cards)) {
            final List<CardType> sortedCards = sortByPowerDesc(removeCardsWithSamePower(cards));

            if (sortedCards.size() == 5) {
                return Combination.of(
                        CombinationType.STRAIT,
                        sortedCards,
                        findBiggerCard(sortedCards).getPowerAsInt()
                );
            }

            while (true) {
                final List<CardType> firstFiveCards = sortedCards.stream()
                        .limit(5)
                        .collect(Collectors.toList());

                if (isStrait(firstFiveCards)) {
                    return Combination.of(
                            CombinationType.STRAIT,
                            firstFiveCards,
                            findBiggerCard(firstFiveCards).getPowerAsInt()
                    );
                }
                sortedCards.remove(0);
            }
        }
        final List<CardType> straitWithAce = checkStraitWithAce(cards);

        if (!straitWithAce.isEmpty()) {
            return Combination.of(
                    CombinationType.STRAIT,
                    straitWithAce,
                    5 // because 5 is bigger card for this combination type
            );
        }
        return Combination.empty();
    }

    private List<CardType> checkStraitWithAce(List<CardType> cards) {

        final List<CardType> distinctList = sortByPowerAsc(removeCardsWithSamePower(cards));

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

    private boolean isStrait(List<CardType> cards) {
        final List<CardType> distinctList = sortByPowerAsc(removeCardsWithSamePower(cards));
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


    private Combination findFlush(List<CardType> cardTypes) {
        final List<CardType> spadeFlush = findFlushBySuit(cardTypes, CardType.SuitType.SPADE);

        if (!spadeFlush.isEmpty()) {
            return Combination.of(
                    CombinationType.FLUSH,
                    sortByPowerDesc(spadeFlush),
                    findBiggerCard(spadeFlush).getPowerAsInt()
            );
        }

        final List<CardType> heartFlush = findFlushBySuit(cardTypes, CardType.SuitType.HEART);

        if (!heartFlush.isEmpty()) {
            return Combination.of(
                    CombinationType.FLUSH,
                    sortByPowerDesc(heartFlush),
                    findBiggerCard(heartFlush).getPowerAsInt()
            );
        }

        final List<CardType> clubFlush = findFlushBySuit(cardTypes, CardType.SuitType.CLUB);

        if (!clubFlush.isEmpty()) {
            return Combination.of(
                    CombinationType.FLUSH,
                    sortByPowerDesc(clubFlush),
                    findBiggerCard(clubFlush).getPowerAsInt()
            );
        }

        final List<CardType> diamondFlush = findFlushBySuit(cardTypes, CardType.SuitType.DIAMOND);

        if (!diamondFlush.isEmpty()) {
            return Combination.of(
                    CombinationType.FLUSH,
                    sortByPowerDesc(diamondFlush),
                    findBiggerCard(diamondFlush).getPowerAsInt()
            );
        }

        return Combination.empty();
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

}
