package ru.sm.poker.service;

import org.junit.Assert;
import org.junit.Test;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.Combination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CheckCombinationServiceTest {

    private final CheckCombinationService checkCombinationService = new CheckCombinationService();

    private static final List<CardType> FLUSH_ROUAL_FULL = new ArrayList<>(Arrays.asList(CardType.A_H, CardType.K_H, CardType.Q_H, CardType.J_H, CardType.TEN_H, CardType.FOUR_H, CardType.K_S));
    private static final List<CardType> FLUSH_ROYAL_COMBINATION = new ArrayList<>(Arrays.asList(CardType.A_H, CardType.K_H, CardType.Q_H, CardType.J_H, CardType.TEN_H));


    private static final List<CardType> STRAIT_FLUSH_FULL = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.SEVEN_C, CardType.TEN_C, CardType.J_C, CardType.NINE_C, CardType.FOUR_H, CardType.K_S));
    private static final List<CardType> STRAIT_FLUSH_COMBINATION = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.SEVEN_C, CardType.TEN_C, CardType.J_C, CardType.NINE_C));

    private static final List<CardType> KARE_FULL = new ArrayList<>(Arrays.asList(CardType.NINE_C, CardType.FOUR_H, CardType.K_S, CardType.SIX_C, CardType.SIX_H, CardType.SIX_S, CardType.SIX_D));
    private static final List<CardType> KARE_COMBINATION = new ArrayList<>(Arrays.asList(CardType.SIX_C, CardType.SIX_H, CardType.SIX_S, CardType.SIX_D, CardType.K_S));


    private static final List<CardType> FLUSH_FULL = new ArrayList<>(Arrays.asList(CardType.NINE_H, CardType.A_H, CardType.K_H, CardType.TWO_H, CardType.FIVE_H, CardType.FOUR_H, CardType.K_S));
    private static final List<CardType> FLUSH_COMBINATION = new ArrayList<>(Arrays.asList(CardType.NINE_H, CardType.A_H, CardType.K_H, CardType.FIVE_H, CardType.FOUR_H));


    private static final List<CardType> FULL_HOUSE_FULL = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.EIGHT_D, CardType.EIGHT_H, CardType.FIVE_D, CardType.FIVE_C, CardType.FIVE_H, CardType.FOUR_H));
    private static final List<CardType> FULL_HOUSE_COMBINATION = new ArrayList<>(Arrays.asList(CardType.FIVE_D, CardType.FIVE_C, CardType.EIGHT_C, CardType.EIGHT_D, CardType.EIGHT_H));


    private static final List<CardType> STRAIT_FULL = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.SEVEN_S, CardType.SIX_H, CardType.FIVE_D, CardType.THREE_D, CardType.FOUR_H, CardType.TWO_D));
    private static final List<CardType> STRAIT_COMBINATION = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.SEVEN_S, CardType.SIX_H, CardType.FIVE_D, CardType.FOUR_H));


    private static final List<CardType> TWO_PAIR_FULL = new ArrayList<>(Arrays.asList(CardType.A_H, CardType.A_D, CardType.FOUR_H, CardType.FOUR_C, CardType.SEVEN_S, CardType.SEVEN_C, CardType.K_D));
    private static final List<CardType> TWO_PAIR_COMBINATION = new ArrayList<>(Arrays.asList(CardType.SEVEN_S, CardType.SEVEN_C, CardType.A_H, CardType.A_D, CardType.K_D));

    private static final List<CardType> PAIR_FULL = new ArrayList<>(Arrays.asList(CardType.THREE_H, CardType.THREE_C, CardType.A_C, CardType.K_S, CardType.TWO_D, CardType.FOUR_D, CardType.SEVEN_S));
    private static final List<CardType> PAIR_FULL_COMBINATION = new ArrayList<>(Arrays.asList(CardType.THREE_H, CardType.THREE_C, CardType.A_C, CardType.K_S, CardType.SEVEN_S));


    private static final List<CardType> THREE_FULL = new ArrayList<>(Arrays.asList(CardType.THREE_H, CardType.THREE_C, CardType.THREE_S, CardType.K_D, CardType.SEVEN_S, CardType.FOUR_H, CardType.NINE_C));
    private static final List<CardType> THREE_COMBINATION = new ArrayList<>(Arrays.asList(CardType.THREE_H, CardType.THREE_C, CardType.THREE_S, CardType.K_D, CardType.NINE_C));

    private static final List<CardType> HIGH_CARD_FULL = new ArrayList<>(Arrays.asList(CardType.TWO_H, CardType.TEN_C, CardType.A_H, CardType.K_D, CardType.SEVEN_S, CardType.FOUR_H, CardType.NINE_C));
    private static final List<CardType> HIGH_COMBINATION = new ArrayList<>(Arrays.asList(CardType.A_H, CardType.K_D, CardType.TEN_C, CardType.NINE_C, CardType.SEVEN_S));


    @Test
    public void testFlushRoyal() {
        final Map<Combination, List<CardType>> flashRoyal = checkCombinationService.findCombination(FLUSH_ROUAL_FULL);
        Assert.assertEquals(flashRoyal.get(Combination.FLUSH_ROYAL), FLUSH_ROYAL_COMBINATION);
    }

    @Test
    public void testStraitFlash() {
        final Map<Combination, List<CardType>> straitFlush = checkCombinationService.findCombination(STRAIT_FLUSH_FULL);
        Assert.assertEquals(straitFlush.get(Combination.STRAIT_FLUSH), STRAIT_FLUSH_COMBINATION);
    }


    @Test
    public void testKare() {
        final Map<Combination, List<CardType>> kare = checkCombinationService.findCombination(KARE_FULL);
        Assert.assertEquals(kare.get(Combination.KARE), KARE_COMBINATION);
    }

    @Test
    public void testFullHouse() {
        final Map<Combination, List<CardType>> fullHouse = checkCombinationService.findCombination(FULL_HOUSE_FULL);
        Assert.assertEquals(fullHouse.get(Combination.FULL_HOUSE), FULL_HOUSE_COMBINATION);
    }

    @Test
    public void testFlush() {
        final Map<Combination, List<CardType>> flush = checkCombinationService.findCombination(FLUSH_FULL);
        Assert.assertEquals(flush.get(Combination.FLUSH), FLUSH_COMBINATION);
    }

    @Test
    public void testStrait() {
        final Map<Combination, List<CardType>> straight = checkCombinationService.findCombination(STRAIT_FULL);
        Assert.assertEquals(straight.get(Combination.STRAIT), STRAIT_COMBINATION);
    }


    @Test
    public void testThree() {
        final Map<Combination, List<CardType>> three = checkCombinationService.findCombination(THREE_FULL);
        Assert.assertEquals(three.get(Combination.THREE), THREE_COMBINATION);
    }

    @Test
    public void testTwoPair() {
        final Map<Combination, List<CardType>> twoPair = checkCombinationService.findCombination(TWO_PAIR_FULL);
        Assert.assertEquals(twoPair.get(Combination.TWO_PAIR), TWO_PAIR_COMBINATION);
    }

    @Test
    public void testOnePair() {
        final Map<Combination, List<CardType>> pair = checkCombinationService.findCombination(PAIR_FULL);
        Assert.assertEquals(pair.get(Combination.PAIR), PAIR_FULL_COMBINATION);
    }

    @Test
    public void testHighCard() {
        final Map<Combination, List<CardType>> highCards = checkCombinationService.findCombination(HIGH_CARD_FULL);
        Assert.assertEquals(highCards.get(Combination.HIGH_CARD), HIGH_COMBINATION);
    }
}