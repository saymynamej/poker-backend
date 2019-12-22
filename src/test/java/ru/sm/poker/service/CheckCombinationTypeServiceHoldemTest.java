package ru.sm.poker.service;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckCombinationTypeServiceHoldemTest {

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
        final Pair<CombinationType, List<CardType>> flashRoyal = checkCombinationServiceHoldem.findCombination(FLUSH_ROUAL_FULL);
        Assert.assertEquals(FLUSH_ROYAL_COMBINATION, flashRoyal.getValue());
    }

    @Test
    public void testStraitFlash() {
        final Pair<CombinationType, List<CardType>> straitFlush = checkCombinationServiceHoldem.findCombination(STRAIT_FLUSH_FULL);
        Assert.assertEquals(STRAIT_FLUSH_COMBINATION, straitFlush.getValue());
    }

    @Test
    public void testStraitFlash2() {
        final Pair<CombinationType, List<CardType>> straitFlush2 = checkCombinationServiceHoldem.findCombination(STRAIT_FLUSH_FULL_2);
        Assert.assertEquals(STRAIT_FLUSH_COMBINATION_2, straitFlush2.getValue());
    }


    @Test
    public void testKare() {
        final Pair<CombinationType, List<CardType>> kare = checkCombinationServiceHoldem.findCombination(KARE_FULL);
        Assert.assertEquals(KARE_COMBINATION, kare.getValue());
    }

    @Test
    public void testFullHouse() {
        final Pair<CombinationType, List<CardType>> fullHouse = checkCombinationServiceHoldem.findCombination(FULL_HOUSE_FULL);
        Assert.assertEquals(FULL_HOUSE_COMBINATION, fullHouse.getValue());
    }

    @Test
    public void testFlush() {
        final Pair<CombinationType, List<CardType>> flush = checkCombinationServiceHoldem.findCombination(FLUSH_FULL);
        Assert.assertEquals(FLUSH_COMBINATION, flush.getValue());
    }

    @Test
    public void testStrait() {
        final Pair<CombinationType, List<CardType>> straight = checkCombinationServiceHoldem.findCombination(STRAIT_FULL);
        Assert.assertEquals(STRAIT_COMBINATION, straight.getValue());
    }

    @Test
    public void testStrait2() {
        final Pair<CombinationType, List<CardType>> straight2 = checkCombinationServiceHoldem.findCombination(STRAIT_FULL_2);
        Assert.assertEquals(STRAIT_COMBINATION_2, straight2.getValue());
    }

    @Test
    public void testStraight3() {
        Pair<CombinationType, List<CardType>> strait3 = checkCombinationServiceHoldem.findCombination(STRAIT_FULL_3);
        Assert.assertEquals(STRAIT_COMBINATION_3, strait3.getValue());
    }


    @Test
    public void testThree() {
        final Pair<CombinationType, List<CardType>> three = checkCombinationServiceHoldem.findCombination(THREE_FULL);
        Assert.assertEquals(THREE_COMBINATION, three.getValue());
    }

    @Test
    public void testThree2() {
        final Pair<CombinationType, List<CardType>> three_2 = checkCombinationServiceHoldem.findCombination(THREE_FULL_2);
        Assert.assertEquals(THREE_COMBINATION_2, three_2.getValue());
    }

    @Test
    public void testThree3() {
        final Pair<CombinationType, List<CardType>> three_3 = checkCombinationServiceHoldem.findCombination(THREE_FULL_3);
        Assert.assertEquals(THREE_COMBINATION_3, three_3.getValue());
    }

    @Test
    public void testTwoPair() {
        final Pair<CombinationType, List<CardType>> twoPair = checkCombinationServiceHoldem.findCombination(TWO_PAIR_FULL);
        Assert.assertEquals(TWO_PAIR_COMBINATION, twoPair.getValue());
    }


    /* ONE PAIR BLOCK TEST
       ONE PAIR BLOCK TEST
       ONE PAIR BLOCK TEST
     */

    @Test
    public void testOnePair() {
        final Pair<CombinationType, List<CardType>> pair = checkCombinationServiceHoldem.findCombination(PAIR_FULL);
        Assert.assertEquals(PAIR_FULL_COMBINATION, pair.getValue());
    }

    /* HIGH CARD BLOCK TEST
       HIGH CARD BLOCK TEST
       HIGH CARD BLOCK TEST
     */
    @Test
    public void testHighCard() {
        final Pair<CombinationType, List<CardType>> highCards = checkCombinationServiceHoldem.findCombination(HIGH_CARD_FULL);
        Assert.assertEquals(HIGH_COMBINATION, highCards.getValue());
    }

    @Test
    public void testHighCard2() {
        final Pair<CombinationType, List<CardType>> highCards_2 = checkCombinationServiceHoldem.findCombination(HIGH_CARD_FULL_2);
        Assert.assertEquals(HIGH_COMBINATION_2, highCards_2.getValue());
    }

}
