package ru.sm.poker.service;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;
import ru.sm.poker.service.holdem.HoldemCombinationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.sm.poker.enums.CardType.*;

public class CheckCombinationDTOTypeServiceHoldemTest {

    private final HoldemCombinationService checkHoldemCombinationService = new HoldemCombinationService();

    private static final List<CardType> FLUSH_ROUAL_FULL = new ArrayList<>(Arrays.asList(A_H, K_H, Q_H, J_H, TEN_H, FOUR_H, K_S));
    private static final List<CardType> FLUSH_ROYAL_COMBINATION = new ArrayList<>(Arrays.asList(A_H, K_H, Q_H, J_H, TEN_H));


    private static final List<CardType> STRAIT_FLUSH_FULL = new ArrayList<>(Arrays.asList(EIGHT_C, SEVEN_C, TEN_C, J_C, NINE_C, FOUR_H, K_S));
    private static final List<CardType> STRAIT_FLUSH_COMBINATION = new ArrayList<>(Arrays.asList(EIGHT_C, SEVEN_C, TEN_C, J_C, NINE_C));


    private static final List<CardType> STRAIT_FLUSH_FULL_2 = new ArrayList<>(Arrays.asList(TWO_H, FOUR_D, EIGHT_C, NINE_C, TEN_C, J_C, Q_C));
    private static final List<CardType> STRAIT_FLUSH_COMBINATION_2 = new ArrayList<>(Arrays.asList(EIGHT_C, NINE_C, TEN_C, J_C, Q_C));

    private static final List<CardType> KARE_FULL = new ArrayList<>(Arrays.asList(NINE_C, FOUR_H, K_S, SIX_C, SIX_H, SIX_S, SIX_D));
    private static final List<CardType> KARE_COMBINATION = new ArrayList<>(Arrays.asList(SIX_C, SIX_H, SIX_S, SIX_D, K_S));


    private static final List<CardType> FLUSH_FULL = new ArrayList<>(Arrays.asList(NINE_H, A_H, K_H, TWO_H, FIVE_H, FOUR_H, K_S));
    private static final List<CardType> FLUSH_COMBINATION = new ArrayList<>(Arrays.asList(NINE_H, A_H, K_H, FIVE_H, FOUR_H));


    private static final List<CardType> FULL_HOUSE_FULL = new ArrayList<>(Arrays.asList(EIGHT_C, EIGHT_D, EIGHT_H, FIVE_D, FIVE_C, FIVE_H, FOUR_H));
    private static final List<CardType> FULL_HOUSE_COMBINATION = new ArrayList<>(Arrays.asList(FIVE_D, FIVE_C, EIGHT_C, EIGHT_D, EIGHT_H));


    private static final List<CardType> STRAIT_FULL = new ArrayList<>(Arrays.asList(EIGHT_C, SEVEN_S, SIX_H, FIVE_D, THREE_D, FOUR_H, TWO_D));
    private static final List<CardType> STRAIT_COMBINATION = new ArrayList<>(Arrays.asList(EIGHT_C, SEVEN_S, SIX_H, FIVE_D, FOUR_H));


    private static final List<CardType> STRAIT_FULL_2 = new ArrayList<>(Arrays.asList(THREE_H, FIVE_C, SEVEN_C, EIGHT_C, NINE_D, TEN_H, J_H));
    private static final List<CardType> STRAIT_COMBINATION_2 = new ArrayList<>(Arrays.asList(J_H, TEN_H, NINE_D, EIGHT_C, SEVEN_C));


    private static final List<CardType> STRAIT_FULL_3 = new ArrayList<>(Arrays.asList(A_D, J_C, TEN_C, Q_C, K_C, J_H, J_D));
    private static final List<CardType> STRAIT_COMBINATION_3 = new ArrayList<>(Arrays.asList(A_D, K_C, Q_C, J_C, TEN_C));


    private static final List<CardType> TWO_PAIR_FULL = new ArrayList<>(Arrays.asList(A_H, A_D, FOUR_H, FOUR_C, SEVEN_S, SEVEN_C, K_D));
    private static final List<CardType> TWO_PAIR_COMBINATION = new ArrayList<>(Arrays.asList(SEVEN_S, SEVEN_C, A_H, A_D, K_D));


    private static final List<CardType> THREE_FULL = new ArrayList<>(Arrays.asList(THREE_H, THREE_C, THREE_S, K_D, SEVEN_S, FOUR_H, NINE_C));
    private static final List<CardType> THREE_COMBINATION = new ArrayList<>(Arrays.asList(THREE_H, THREE_C, THREE_S, K_D, NINE_C));

    private static final List<CardType> THREE_FULL_2 = new ArrayList<>(Arrays.asList(SEVEN_S, FOUR_H, NINE_C, A_D, A_C, A_H, K_D));
    private static final List<CardType> THREE_COMBINATION_2 = new ArrayList<>(Arrays.asList(A_D, A_C, A_H, K_D, NINE_C));


    private static final List<CardType> THREE_FULL_3 = new ArrayList<>(Arrays.asList(SEVEN_S, FOUR_H, A_D, NINE_C, A_C, A_H, K_D));
    private static final List<CardType> THREE_COMBINATION_3 = new ArrayList<>(Arrays.asList(A_D, A_C, A_H, K_D, NINE_C));


    private static final List<CardType> PAIR_FULL = new ArrayList<>(Arrays.asList(THREE_H, THREE_C, A_C, K_S, TWO_D, FOUR_D, SEVEN_S));
    private static final List<CardType> PAIR_FULL_COMBINATION = new ArrayList<>(Arrays.asList(THREE_H, THREE_C, A_C, K_S, SEVEN_S));


    private static final List<CardType> HIGH_CARD_FULL = new ArrayList<>(Arrays.asList(TWO_H, TEN_C, A_H, K_D, SEVEN_S, FOUR_H, NINE_C));
    private static final List<CardType> HIGH_COMBINATION = new ArrayList<>(Arrays.asList(A_H, K_D, TEN_C, NINE_C, SEVEN_S));

    private static final List<CardType> HIGH_CARD_FULL_2 = new ArrayList<>(Arrays.asList(A_C, TEN_C, FIVE_H, K_D, EIGHT_H, FOUR_H, NINE_C));
    private static final List<CardType> HIGH_COMBINATION_2 = new ArrayList<>(Arrays.asList(A_C, K_D, TEN_C, NINE_C, EIGHT_H));


    @Test
    public void testFlushRoyal() {
        final Pair<CombinationType, List<CardType>> flashRoyal = checkHoldemCombinationService.findCombination(FLUSH_ROUAL_FULL);
        Assert.assertEquals(FLUSH_ROYAL_COMBINATION, flashRoyal.getValue());
    }

    @Test
    public void testStraitFlash() {
        final Pair<CombinationType, List<CardType>> straitFlush = checkHoldemCombinationService.findCombination(STRAIT_FLUSH_FULL);
        Assert.assertEquals(STRAIT_FLUSH_COMBINATION, straitFlush.getValue());
    }

    @Test
    public void testStraitFlash2() {
        final Pair<CombinationType, List<CardType>> straitFlush2 = checkHoldemCombinationService.findCombination(STRAIT_FLUSH_FULL_2);
        Assert.assertEquals(STRAIT_FLUSH_COMBINATION_2, straitFlush2.getValue());
    }


    @Test
    public void testKare() {
        final Pair<CombinationType, List<CardType>> kare = checkHoldemCombinationService.findCombination(KARE_FULL);
        Assert.assertEquals(KARE_COMBINATION, kare.getValue());
    }

    @Test
    public void testFullHouse() {
        final Pair<CombinationType, List<CardType>> fullHouse = checkHoldemCombinationService.findCombination(FULL_HOUSE_FULL);
        Assert.assertEquals(FULL_HOUSE_COMBINATION, fullHouse.getValue());
    }

    @Test
    public void testFlush() {
        final Pair<CombinationType, List<CardType>> flush = checkHoldemCombinationService.findCombination(FLUSH_FULL);
        Assert.assertEquals(FLUSH_COMBINATION, flush.getValue());
    }

    @Test
    public void testStrait() {
        final Pair<CombinationType, List<CardType>> straight = checkHoldemCombinationService.findCombination(STRAIT_FULL);
        Assert.assertEquals(STRAIT_COMBINATION, straight.getValue());
    }

    @Test
    public void testStrait2() {
        final Pair<CombinationType, List<CardType>> straight2 = checkHoldemCombinationService.findCombination(STRAIT_FULL_2);
        Assert.assertEquals(STRAIT_COMBINATION_2, straight2.getValue());
    }

    @Test
    public void testStraight3() {
        Pair<CombinationType, List<CardType>> strait3 = checkHoldemCombinationService.findCombination(STRAIT_FULL_3);
        Assert.assertEquals(STRAIT_COMBINATION_3, strait3.getValue());
    }


    @Test
    public void testThree() {
        final Pair<CombinationType, List<CardType>> three = checkHoldemCombinationService.findCombination(THREE_FULL);
        Assert.assertEquals(THREE_COMBINATION, three.getValue());
    }

    @Test
    public void testThree2() {
        final Pair<CombinationType, List<CardType>> three_2 = checkHoldemCombinationService.findCombination(THREE_FULL_2);
        Assert.assertEquals(THREE_COMBINATION_2, three_2.getValue());
    }

    @Test
    public void testThree3() {
        final Pair<CombinationType, List<CardType>> three_3 = checkHoldemCombinationService.findCombination(THREE_FULL_3);
        Assert.assertEquals(THREE_COMBINATION_3, three_3.getValue());
    }

    @Test
    public void testTwoPair() {
        final Pair<CombinationType, List<CardType>> twoPair = checkHoldemCombinationService.findCombination(TWO_PAIR_FULL);
        Assert.assertEquals(TWO_PAIR_COMBINATION, twoPair.getValue());
    }


    /* ONE PAIR BLOCK TEST
       ONE PAIR BLOCK TEST
       ONE PAIR BLOCK TEST
     */

    @Test
    public void testOnePair() {
        final Pair<CombinationType, List<CardType>> pair = checkHoldemCombinationService.findCombination(PAIR_FULL);
        Assert.assertEquals(PAIR_FULL_COMBINATION, pair.getValue());
    }

    /* HIGH CARD BLOCK TEST
       HIGH CARD BLOCK TEST
       HIGH CARD BLOCK TEST
     */
    @Test
    public void testHighCard() {
        final Pair<CombinationType, List<CardType>> highCards = checkHoldemCombinationService.findCombination(HIGH_CARD_FULL);
        Assert.assertEquals(HIGH_COMBINATION, highCards.getValue());
    }

    @Test
    public void testHighCard2() {
        final Pair<CombinationType, List<CardType>> highCards_2 = checkHoldemCombinationService.findCombination(HIGH_CARD_FULL_2);
        Assert.assertEquals(HIGH_COMBINATION_2, highCards_2.getValue());
    }

}
