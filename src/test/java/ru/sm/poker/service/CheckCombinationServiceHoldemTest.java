package ru.sm.poker.service;

import org.junit.Assert;
import org.junit.Test;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.Combination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CheckCombinationServiceHoldemTest {

    private final CheckCombinationServiceHoldem checkCombinationServiceHoldem = new CheckCombinationServiceHoldem();

    private static final List<CardType> FLUSH_ROUAL_FULL = new ArrayList<>(Arrays.asList(CardType.A_H, CardType.K_H, CardType.Q_H, CardType.J_H, CardType.TEN_H, CardType.FOUR_H, CardType.K_S));
    private static final List<CardType> FLUSH_ROYAL_COMBINATION = new ArrayList<>(Arrays.asList(CardType.A_H, CardType.K_H, CardType.Q_H, CardType.J_H, CardType.TEN_H));


    private static final List<CardType> STRAIT_FLUSH_FULL = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.SEVEN_C, CardType.TEN_C, CardType.J_C, CardType.NINE_C, CardType.FOUR_H, CardType.K_S));
    private static final List<CardType> STRAIT_FLUSH_COMBINATION = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.SEVEN_C, CardType.TEN_C, CardType.J_C, CardType.NINE_C));


    private static final List<CardType> STRAIT_FLUSH_FULL_2 = new ArrayList<>(Arrays.asList(CardType.TWO_H, CardType.FOUR_D, CardType.EIGHT_C, CardType.NINE_C, CardType.TEN_C, CardType.J_C, CardType.Q_C));
    private static final List<CardType> STRAIT_FLUSH_COMBINATION_2 = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.NINE_C, CardType.TEN_C, CardType.J_C, CardType.Q_C));

    private static final List<CardType> KARE_FULL = new ArrayList<>(Arrays.asList(CardType.NINE_C, CardType.FOUR_H, CardType.K_S, CardType.SIX_C, CardType.SIX_H, CardType.SIX_S, CardType.SIX_D));
    private static final List<CardType> KARE_COMBINATION = new ArrayList<>(Arrays.asList(CardType.SIX_C, CardType.SIX_H, CardType.SIX_S, CardType.SIX_D, CardType.K_S));


    private static final List<CardType> FLUSH_FULL = new ArrayList<>(Arrays.asList(CardType.NINE_H, CardType.A_H, CardType.K_H, CardType.TWO_H, CardType.FIVE_H, CardType.FOUR_H, CardType.K_S));
    private static final List<CardType> FLUSH_COMBINATION = new ArrayList<>(Arrays.asList(CardType.NINE_H, CardType.A_H, CardType.K_H, CardType.FIVE_H, CardType.FOUR_H));


    private static final List<CardType> FULL_HOUSE_FULL = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.EIGHT_D, CardType.EIGHT_H, CardType.FIVE_D, CardType.FIVE_C, CardType.FIVE_H, CardType.FOUR_H));
    private static final List<CardType> FULL_HOUSE_COMBINATION = new ArrayList<>(Arrays.asList(CardType.FIVE_D, CardType.FIVE_C, CardType.EIGHT_C, CardType.EIGHT_D, CardType.EIGHT_H));


    private static final List<CardType> STRAIT_FULL = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.SEVEN_S, CardType.SIX_H, CardType.FIVE_D, CardType.THREE_D, CardType.FOUR_H, CardType.TWO_D));
    private static final List<CardType> STRAIT_COMBINATION = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.SEVEN_S, CardType.SIX_H, CardType.FIVE_D, CardType.FOUR_H));


    private static final List<CardType> STRAIT_FULL_2 = new ArrayList<>(Arrays.asList(CardType.THREE_H, CardType.FIVE_C, CardType.SEVEN_C, CardType.EIGHT_C, CardType.NINE_D, CardType.TEN_H, CardType.J_H));
    private static final List<CardType> STRAIT_COMBINATION_2 = new ArrayList<>(Arrays.asList(CardType.J_H, CardType.TEN_H, CardType.NINE_D, CardType.EIGHT_C, CardType.SEVEN_C));


    private static final List<CardType> STRAIT_FULL_3 = new ArrayList<>(Arrays.asList(CardType.A_D, CardType.J_C, CardType.TEN_C, CardType.Q_C, CardType.K_C, CardType.J_H, CardType.J_D));
    private static final List<CardType> STRAIT_COMBINATION_3 = new ArrayList<>(Arrays.asList(CardType.A_D, CardType.K_C, CardType.Q_C, CardType.J_C, CardType.TEN_C));


    private static final List<CardType> TWO_PAIR_FULL = new ArrayList<>(Arrays.asList(CardType.A_H, CardType.A_D, CardType.FOUR_H, CardType.FOUR_C, CardType.SEVEN_S, CardType.SEVEN_C, CardType.K_D));
    private static final List<CardType> TWO_PAIR_COMBINATION = new ArrayList<>(Arrays.asList(CardType.SEVEN_S, CardType.SEVEN_C, CardType.A_H, CardType.A_D, CardType.K_D));


    private static final List<CardType> THREE_FULL = new ArrayList<>(Arrays.asList(CardType.THREE_H, CardType.THREE_C, CardType.THREE_S, CardType.K_D, CardType.SEVEN_S, CardType.FOUR_H, CardType.NINE_C));
    private static final List<CardType> THREE_COMBINATION = new ArrayList<>(Arrays.asList(CardType.THREE_H, CardType.THREE_C, CardType.THREE_S, CardType.K_D, CardType.NINE_C));

    private static final List<CardType> THREE_FULL_2 = new ArrayList<>(Arrays.asList(CardType.SEVEN_S, CardType.FOUR_H, CardType.NINE_C, CardType.A_D, CardType.A_C, CardType.A_H, CardType.K_D));
    private static final List<CardType> THREE_COMBINATION_2 = new ArrayList<>(Arrays.asList(CardType.A_D, CardType.A_C, CardType.A_H, CardType.K_D, CardType.NINE_C));


    private static final List<CardType> THREE_FULL_3 = new ArrayList<>(Arrays.asList(CardType.SEVEN_S, CardType.FOUR_H, CardType.A_D, CardType.NINE_C, CardType.A_C, CardType.A_H, CardType.K_D));
    private static final List<CardType> THREE_COMBINATION_3 = new ArrayList<>(Arrays.asList(CardType.A_D, CardType.A_C, CardType.A_H, CardType.K_D, CardType.NINE_C));


    private static final List<CardType> PAIR_FULL = new ArrayList<>(Arrays.asList(CardType.THREE_H, CardType.THREE_C, CardType.A_C, CardType.K_S, CardType.TWO_D, CardType.FOUR_D, CardType.SEVEN_S));
    private static final List<CardType> PAIR_FULL_COMBINATION = new ArrayList<>(Arrays.asList(CardType.THREE_H, CardType.THREE_C, CardType.A_C, CardType.K_S, CardType.SEVEN_S));


    private static final List<CardType> HIGH_CARD_FULL = new ArrayList<>(Arrays.asList(CardType.TWO_H, CardType.TEN_C, CardType.A_H, CardType.K_D, CardType.SEVEN_S, CardType.FOUR_H, CardType.NINE_C));
    private static final List<CardType> HIGH_COMBINATION = new ArrayList<>(Arrays.asList(CardType.A_H, CardType.K_D, CardType.TEN_C, CardType.NINE_C, CardType.SEVEN_S));

    private static final List<CardType> HIGH_CARD_FULL_2 = new ArrayList<>(Arrays.asList(CardType.A_C, CardType.TEN_C, CardType.FIVE_H, CardType.K_D, CardType.EIGHT_H, CardType.FOUR_H, CardType.NINE_C));
    private static final List<CardType> HIGH_COMBINATION_2 = new ArrayList<>(Arrays.asList(CardType.A_C, CardType.K_D, CardType.TEN_C, CardType.NINE_C, CardType.EIGHT_H));


    @Test
    public void testFlushRoyal() {
        final Map<Combination, List<CardType>> flashRoyal = checkCombinationServiceHoldem.findCombination(FLUSH_ROUAL_FULL);
        Assert.assertEquals(1, flashRoyal.size());
        Assert.assertEquals(FLUSH_ROYAL_COMBINATION, flashRoyal.get(Combination.FLUSH_ROYAL));
    }

    @Test
    public void testStraitFlash() {
        final Map<Combination, List<CardType>> straitFlush = checkCombinationServiceHoldem.findCombination(STRAIT_FLUSH_FULL);
        Assert.assertEquals(1, straitFlush.size());
        Assert.assertEquals(STRAIT_FLUSH_COMBINATION, straitFlush.get(Combination.STRAIT_FLUSH));
    }

    @Test
    public void testStraitFlash2() {
        final Map<Combination, List<CardType>> straitFlush2 = checkCombinationServiceHoldem.findCombination(STRAIT_FLUSH_FULL_2);
        Assert.assertEquals(1, straitFlush2.size());
        Assert.assertEquals(STRAIT_FLUSH_COMBINATION_2, straitFlush2.get(Combination.STRAIT_FLUSH));
    }


    @Test
    public void testKare() {
        final Map<Combination, List<CardType>> kare = checkCombinationServiceHoldem.findCombination(KARE_FULL);
        Assert.assertEquals(1, kare.size());
        Assert.assertEquals(KARE_COMBINATION, kare.get(Combination.KARE));
    }

    @Test
    public void testFullHouse() {
        final Map<Combination, List<CardType>> fullHouse = checkCombinationServiceHoldem.findCombination(FULL_HOUSE_FULL);
        Assert.assertEquals(1, fullHouse.size());
        Assert.assertEquals(FULL_HOUSE_COMBINATION, fullHouse.get(Combination.FULL_HOUSE));
    }

    @Test
    public void testFlush() {
        final Map<Combination, List<CardType>> flush = checkCombinationServiceHoldem.findCombination(FLUSH_FULL);
        Assert.assertEquals(1, flush.size());
        Assert.assertEquals(FLUSH_COMBINATION, flush.get(Combination.FLUSH));
    }

    @Test
    public void testStrait() {
        final Map<Combination, List<CardType>> straight = checkCombinationServiceHoldem.findCombination(STRAIT_FULL);
        Assert.assertEquals(1, straight.size());
        Assert.assertEquals(STRAIT_COMBINATION, straight.get(Combination.STRAIT));
    }

    @Test
    public void testStrait2() {
        final Map<Combination, List<CardType>> straight2 = checkCombinationServiceHoldem.findCombination(STRAIT_FULL_2);
        Assert.assertEquals(1, straight2.size());
        Assert.assertEquals(STRAIT_COMBINATION_2, straight2.get(Combination.STRAIT));
    }

    @Test
    public void testStraight3() {
        Map<Combination, List<CardType>> strait3 = checkCombinationServiceHoldem.findCombination(STRAIT_FULL_3);
        Assert.assertEquals(1, strait3.size());
        Assert.assertEquals(STRAIT_COMBINATION_3, strait3.get(Combination.STRAIT));
    }


    @Test
    public void testThree() {
        final Map<Combination, List<CardType>> three = checkCombinationServiceHoldem.findCombination(THREE_FULL);
        Assert.assertEquals(1, three.size());
        Assert.assertEquals(THREE_COMBINATION, three.get(Combination.THREE));
    }

    @Test
    public void testThree2() {
        final Map<Combination, List<CardType>> three_2 = checkCombinationServiceHoldem.findCombination(THREE_FULL_2);
        Assert.assertEquals(1, three_2.size());
        Assert.assertEquals(THREE_COMBINATION_2, three_2.get(Combination.THREE));
    }

    @Test
    public void testThree3() {
        final Map<Combination, List<CardType>> three_3 = checkCombinationServiceHoldem.findCombination(THREE_FULL_3);
        Assert.assertEquals(1, three_3.size());
        Assert.assertEquals(THREE_COMBINATION_3, three_3.get(Combination.THREE));
    }

    @Test
    public void testTwoPair() {
        final Map<Combination, List<CardType>> twoPair = checkCombinationServiceHoldem.findCombination(TWO_PAIR_FULL);
        Assert.assertEquals(1, twoPair.size());
        Assert.assertEquals(TWO_PAIR_COMBINATION, twoPair.get(Combination.TWO_PAIR));
    }


    /* ONE PAIR BLOCK TEST
       ONE PAIR BLOCK TEST
       ONE PAIR BLOCK TEST
     */

    @Test
    public void testOnePair() {
        final Map<Combination, List<CardType>> pair = checkCombinationServiceHoldem.findCombination(PAIR_FULL);
        Assert.assertEquals(1, pair.size());
        Assert.assertEquals(PAIR_FULL_COMBINATION, pair.get(Combination.PAIR));
    }

    /* HIGH CARD BLOCK TEST
       HIGH CARD BLOCK TEST
       HIGH CARD BLOCK TEST
     */
    @Test
    public void testHighCard() {
        final Map<Combination, List<CardType>> highCards = checkCombinationServiceHoldem.findCombination(HIGH_CARD_FULL);
        Assert.assertEquals(1, highCards.size());
        Assert.assertEquals(HIGH_COMBINATION, highCards.get(Combination.HIGH_CARD));
    }

    @Test
    public void testHighCard2() {
        final Map<Combination, List<CardType>> highCards_2 = checkCombinationServiceHoldem.findCombination(HIGH_CARD_FULL_2);
        Assert.assertEquals(1, highCards_2.size());
        Assert.assertEquals(HIGH_COMBINATION_2, highCards_2.get(Combination.HIGH_CARD));
    }

}