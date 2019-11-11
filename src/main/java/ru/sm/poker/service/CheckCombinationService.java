package ru.sm.poker.service;

import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.Combination;

import java.util.*;
import java.util.stream.Collectors;

public class CheckCombinationService {

    private final static int A_POWER = 14;
    private final static int K_POWER = 13;
    private final static int Q_POWER = 12;
    private final static int J_POWER = 11;
    private final static int TEN_POWER = 10;
    private final static int COMBINATION_SIZE = 5;
    private final static int KARE_SIZE = 4;


    public Map<Combination, List<CardType>> findCombination(List<CardType> cards){
        final Map<Combination, List<CardType>> combinations = new HashMap<>();

        //FLUSH ROYAL
        final List<CardType> flushRoyal = findFlushRoyal(cards);

        if (!flushRoyal.isEmpty() && checkSize(flushRoyal) && checkDuplicate(flushRoyal)){
            combinations.put(Combination.FLUSH_ROYAL, flushRoyal);
            return combinations;
        }
        //STRAIGHT FLUSH

        final List<CardType> straitFlush = findStraitFlush(cards);
        if (!straitFlush.isEmpty() && checkSize(straitFlush) && checkDuplicate(straitFlush)){
            combinations.put(Combination.STRAIT_FLUSH, straitFlush);
            return combinations;
        }

        //KARE
        final List<CardType> kare = findKare(cards);
        if (!kare.isEmpty()  && checkSize(kare) && checkDuplicate(kare)){
            combinations.put(Combination.KARE, kare);
            return combinations;
        }

        //FULL-HOUSE

        //FLUSH
        final List<CardType> flush = findFlush(cards);
        if (!flush.isEmpty() && checkSize(flush) && checkDuplicate(flush)){
            combinations.put(Combination.FLUSH, flush);
            return combinations;
        }


        //STRAIT

        final List<CardType> strait = findStrait(cards);
        if (!strait.isEmpty() && checkSize(strait) && checkDuplicate(strait)){
            combinations.put(Combination.STRAIT, strait);
            return combinations;
        }

        //THREE

        //TWO PAIR

        //PAIR

        //HIGH CARD

        return combinations;
    }


    private List<CardType> findStraitFlush(List<CardType> cardTypes){
        final List<CardType> flush = findFlush(cardTypes);
        if (checkStrait(flush)){
            return flush;
        }
        return Collections.emptyList();
    }

    private List<CardType> findKare(List<CardType> cards){
        final List<CardType> foundKare = new ArrayList<>();

        for (CardType card : cards) {
            final List<CardType> kare = cards
                    .stream()
                    .filter(cardTypeN -> cardTypeN.getPower() == card.getPower())
                    .collect(Collectors.toList());
            if (kare.size() == KARE_SIZE){
                foundKare.addAll(kare);
            }
        }

        if (foundKare.isEmpty()){
            return Collections.emptyList();
        }

        final List<CardType> distinctKare = foundKare
                .stream()
                .distinct().collect(Collectors.toList());

        distinctKare.add(findBiggerCardWithFilter(cards, foundKare.get(0).getPower()));

        return distinctKare;

    }

    private List<CardType> findFlushRoyal(List<CardType> cardTypes){
        final List<CardType> flush = findFlush(cardTypes);
        if (checkFlushRoyal(flush)){
            return flush;
        }
       return Collections.emptyList();
    }


    private boolean checkFlushRoyal(List<CardType> cardTypes){
        final List<CardType> flushRoyal = cardTypes.stream().filter(cardType -> cardType.getPower() == A_POWER
                || cardType.getPower() == K_POWER
                || cardType.getPower() == Q_POWER
                || cardType.getPower() == J_POWER
                || cardType.getPower() == TEN_POWER).collect(Collectors.toList());

        return flushRoyal.size() == COMBINATION_SIZE;
    }

    private List<CardType> findStrait(List<CardType> cardTypes){
        if (checkStrait(cardTypes)){
            cut(cardTypes);
           return cardTypes;
        }
        return Collections.emptyList();
    }

    private boolean checkStrait(List<CardType> cards){
        final List<CardType> strait = cards
                .stream()
                .sorted(Comparator.comparingInt(CardType::getPower))
                .collect(Collectors.toList());
        boolean isStrait = false;

        for (int i = 0; i < strait.size(); i++) {
            if (i + 1 != strait.size()) {
                isStrait = strait.get(i).getPower() + 1 == strait.get(i + 1).getPower();
                if (!isStrait){
                    break;
                }
            }
        }
        return isStrait;
    }



    private List<CardType> findFlush(List<CardType> cardTypes){

        final List<CardType> spadeFlush = findFlushBySuit(cardTypes, CardType.SuitType.SPADE);

        if (!spadeFlush.isEmpty()){
            return spadeFlush;
        }

        final List<CardType> heartFlush = findFlushBySuit(cardTypes, CardType.SuitType.HEART);

        if (!heartFlush.isEmpty()){
            return heartFlush;
        }

        final List<CardType> clubFlush = findFlushBySuit(cardTypes, CardType.SuitType.CLUB);

        if (!clubFlush.isEmpty()){
            return clubFlush;
        }

        final List<CardType> diamondFlush = findFlushBySuit(cardTypes, CardType.SuitType.DIAMOND);

        if (!diamondFlush.isEmpty()){
            return diamondFlush;
        }

        return Collections.emptyList();
    }

    private List<CardType> findFlushBySuit(List<CardType> cardTypes, CardType.SuitType suitType){
        final List<CardType> cards = cardTypes
                .stream()
                .filter(cardType -> cardType.getSuitType().equals(suitType))
                .collect(Collectors.toList());
        if (cards.size() >= COMBINATION_SIZE){
            if (cards.size() > COMBINATION_SIZE){
               return cut(cards);
            }
            return cards;
        }
        return Collections.emptyList();
    }


    private List<CardType> cut(List<CardType> cards){
        if (cards.size() > COMBINATION_SIZE) {
            cards.remove(findSmallerCard(cards));
            cut(cards);
        }
        return cards;
    }

    private CardType findSmallerCard(List<CardType> cardTypes){
        return cardTypes
                .stream()
                .min(Comparator.comparingInt(CardType::getPower))
                .orElseThrow(() -> new RuntimeException("cannot find min card"));
    }

    private CardType findBiggerCardWithFilter(List<CardType> cardTypes, int filter){
        return cardTypes
                .stream()
                .filter(cardType -> cardType.getPower() != (filter))
                .max(Comparator.comparingInt(CardType::getPower))
                .orElseThrow(() -> new RuntimeException("cannot find min card"));
    }

    private boolean checkSize(List<CardType> cards){
        if (cards.size() == COMBINATION_SIZE){
            return true;
        }
        throw new RuntimeException(String.format("Global warning, in process checking size must be: %s, but : %s", COMBINATION_SIZE, cards.size()));
    }

    private boolean checkDuplicate(List<CardType> cards){
        if (cards
                .stream()
                .distinct()
                .collect(Collectors.toList())
                .size() == 5){
            return true;
        }
        throw new RuntimeException("Global warning, in process checking duplicates cards");
    }
}
