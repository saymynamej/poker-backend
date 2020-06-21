package ru.sm.poker.service.holdem;

import org.springframework.stereotype.Service;
import ru.sm.poker.dto.CombinationDTO;
import ru.sm.poker.dto.PlayerCombinationDTO;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;
import ru.sm.poker.enums.PowerType;
import ru.sm.poker.service.CombinationService;

import java.util.*;
import java.util.stream.Collectors;

import static ru.sm.poker.dto.CombinationDTO.of;

@Service
public class HoldemCombinationService implements CombinationService {
    private final static int COMBINATION_SIZE = 5;
    private final static int FULL_COMBINATION_SIZE = 7;
    private final static int KARE_SIZE = 4;
    private final static int PAIR_SIZE = 2;
    private final static int THREE_SIZE = 3;

    @Override
    public List<PlayerCombinationDTO> findWinners(List<PlayerCombinationDTO> playersAndCombinations) {
        final Integer maxPowerOfCombination = playersAndCombinations.stream()
                .map(player -> player.getCombination().getCombinationType().getPower())
                .max(Comparator.comparingInt(value -> value)).orElseThrow();

        final List<PlayerCombinationDTO> winners = playersAndCombinations.stream()
                .filter(playerCombinationDTO -> playerCombinationDTO.getCombination().getCombinationType().getPower() == maxPowerOfCombination)
                .collect(Collectors.toList());

        if (winners.size() == 1) {
            return winners;
        }

        final int max = findMaxSumCards(playersAndCombinations);
        return winners.stream()
                .filter(playerCombination -> sumCards(playerCombination.getCombination().getCards()) == max)
                .collect(Collectors.toList());
    }

    @Override
    public CombinationDTO findCombination(List<CardType> cards) {
        if (cards.size() != FULL_COMBINATION_SIZE) {
            throw new RuntimeException("cards size must be 7");
        }

        final List<CardType> flushRoyal = findFlushRoyal(new ArrayList<>(cards));
        if (!flushRoyal.isEmpty() && checkSize(flushRoyal) && isDuplicateExist(flushRoyal)) {
            return of(CombinationType.FLUSH_ROYAL, flushRoyal);
        }

        final List<CardType> straitFlush = findStraitFlush(new ArrayList<>(cards));
        if (!straitFlush.isEmpty() && checkSize(straitFlush) && isDuplicateExist(straitFlush)) {
            return of(CombinationType.STRAIT_FLUSH, straitFlush);
        }

        final List<CardType> kare = findKare(new ArrayList<>(cards));
        if (!kare.isEmpty() && checkSize(kare) && isDuplicateExist(kare)) {
            return CombinationDTO.of(CombinationType.KARE, kare);
        }

        final List<CardType> fullHouse = findFullHouse(new ArrayList<>(cards));
        if (!fullHouse.isEmpty() && checkSize(fullHouse) && isDuplicateExist(fullHouse)) {
            return CombinationDTO.of(CombinationType.FULL_HOUSE, fullHouse);
        }

        final List<CardType> flush = findFlush(new ArrayList<>(cards));
        if (!flush.isEmpty() && checkSize(flush) && isDuplicateExist(flush)) {
            return CombinationDTO.of(CombinationType.FLUSH, flush);
        }

        final List<CardType> strait = findStrait(new ArrayList<>(cards));
        if (!strait.isEmpty() && checkSize(strait) && isDuplicateExist(strait)) {
            return CombinationDTO.of(CombinationType.STRAIT, strait);
        }

        final List<CardType> three = findThree(new ArrayList<>(cards));
        if (!three.isEmpty() && checkSize(three) && isDuplicateExist(three)) {
            return CombinationDTO.of(CombinationType.THREE, three);
        }

        final List<CardType> twoPair = findTwoPair(new ArrayList<>(cards));
        if (!twoPair.isEmpty() && checkSize(twoPair) && isDuplicateExist(twoPair)) {
            return CombinationDTO.of(CombinationType.TWO_PAIR, twoPair);
        }

        final List<CardType> pair = findPair(new ArrayList<>(cards));

        if (!pair.isEmpty() && checkSize(pair) && isDuplicateExist(pair)) {
            return CombinationDTO.of(CombinationType.PAIR, pair);
        }

        final List<CardType> highCards = findHighCard(new ArrayList<>(cards));

        if (!highCards.isEmpty() && checkSize(highCards) && isDuplicateExist(highCards)) {
            return CombinationDTO.of(CombinationType.HIGH_CARD, highCards);
        }

        throw new RuntimeException("global error, could not found poker combination!");
    }

    private int findMaxSumCards(List<PlayerCombinationDTO> playersAndCombinations) {
        return playersAndCombinations.stream()
                .map(playerCombination -> sumCards(playerCombination.getCombination().getCards()))
                .max(Comparator.comparingInt(num -> num))
                .orElseThrow(() -> new RuntimeException("cannot find max cards"));
    }

    private int sumCards(List<CardType> cards) {
        return cards.stream().map(CardType::getPowerAsInt)
                .reduce(Integer::sum)
                .orElseThrow(() -> new RuntimeException("cannot calculate sum"));
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

        if (!flush.isEmpty() && isStrait(flush)) {
            return sortByPowerDesc(flush);
        }

        final List<CardType> straitWithAce = checkStraitWithAce(cardTypes);
        if (!straitWithAce.isEmpty()) {
            final List<CardType> straitFlushWithAce = findFlush(straitWithAce);
            if (!straitFlushWithAce.isEmpty()) {
                return straitFlushWithAce;
            }
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
            return sortByPowerDesc(flush);
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


    private List<CardType> findStrait(List<CardType> cards) {
        if (isStrait(cards)) {
            final List<CardType> sortedCards = sortByPowerDesc(removeCardsWithSamePower(cards));

            if (sortedCards.size() == 5) {
                return sortedCards;
            }

            while (true) {
                final List<CardType> firstFiveCards = sortedCards.stream()
                        .limit(5)
                        .collect(Collectors.toList());

                if (isStrait(firstFiveCards)) {
                    return firstFiveCards;
                }
                sortedCards.remove(0);
            }
        }
        final List<CardType> straitWithAce = checkStraitWithAce(cards);

        if (!straitWithAce.isEmpty()) {
            return straitWithAce;
        }
        return Collections.emptyList();
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


    private List<CardType> findFlush(List<CardType> cardTypes) {
        final List<CardType> spadeFlush = findFlushBySuit(cardTypes, CardType.SuitType.SPADE);

        if (!spadeFlush.isEmpty()) {
            return sortByPowerDesc(spadeFlush);
        }

        final List<CardType> heartFlush = findFlushBySuit(cardTypes, CardType.SuitType.HEART);

        if (!heartFlush.isEmpty()) {
            return sortByPowerDesc(heartFlush);
        }

        final List<CardType> clubFlush = findFlushBySuit(cardTypes, CardType.SuitType.CLUB);

        if (!clubFlush.isEmpty()) {
            return sortByPowerDesc(clubFlush);
        }

        final List<CardType> diamondFlush = findFlushBySuit(cardTypes, CardType.SuitType.DIAMOND);

        if (!diamondFlush.isEmpty()) {
            return sortByPowerDesc(diamondFlush);
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

    private boolean isDuplicateExist(List<CardType> cards) {
        if (cards.stream()
                .distinct()
                .count() == COMBINATION_SIZE) {
            return true;
        }
        throw new RuntimeException("Global warning, in process checking duplicates cards");
    }

}
