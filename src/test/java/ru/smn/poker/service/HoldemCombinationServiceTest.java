package ru.smn.poker.service;

import org.junit.Assert;
import org.junit.Test;
import ru.smn.poker.dto.Combination;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.enums.CombinationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.smn.poker.enums.CardType.*;

public class HoldemCombinationServiceTest {

    private final HoldemCombinationService checkHoldemCombinationService = new HoldemCombinationService();

    private static final List<CardType> FLUSH_ROYAL_HEART_FULL = new ArrayList<>(Arrays.asList(A_H, K_H, Q_H, J_H, TEN_H, FOUR_H, K_S));
    private static final List<CardType> FLUSH_ROYAL_HEART_COMBINATION = new ArrayList<>(Arrays.asList(A_H, K_H, Q_H, J_H, TEN_H));

    private static final List<CardType> FLUSH_ROYAL_SPADE_FULL = new ArrayList<>(Arrays.asList(FOUR_H, TWO_H, A_S, K_S, Q_S, J_S, TEN_S));
    private static final List<CardType> FLUSH_ROYAL_SPADE_COMBINATION = new ArrayList<>(Arrays.asList(A_S, K_S, Q_S, J_S, TEN_S));

    private static final List<CardType> FLUSH_ROYAL_DIAMOND_FULL = new ArrayList<>(Arrays.asList(TWO_C, TEN_D, A_D, K_D, Q_D, J_D, SEVEN_C));
    private static final List<CardType> FLUSH_ROYAL_DIAMOND_COMBINATION = new ArrayList<>(Arrays.asList(A_D, K_D, Q_D, J_D, TEN_D));

    private static final List<CardType> FLUSH_ROYAL_CLUB_FULL = new ArrayList<>(Arrays.asList(A_H, K_H, TEN_C, J_C, Q_C, K_C, A_C));
    private static final List<CardType> FLUSH_ROYAL_CLUB_COMBINATION = new ArrayList<>(Arrays.asList(A_C, K_C, Q_C, J_C, TEN_C));


    private static final List<CardType> STRAIT_FLUSH_CLUB_FULL = new ArrayList<>(Arrays.asList(EIGHT_C, SEVEN_C, TEN_C, J_C, NINE_C, FOUR_H, K_S));
    private static final List<CardType> STRAIT_FLUSH_CLUB_COMBINATION = new ArrayList<>(Arrays.asList(J_C, TEN_C, NINE_C, EIGHT_C, SEVEN_C));

    private static final List<CardType> STRAIT_FLUSH_HEART_FULL = new ArrayList<>(Arrays.asList(TWO_H, FOUR_D, EIGHT_H, NINE_H, TEN_H, J_H, Q_H));
    private static final List<CardType> STRAIT_FLUSH_HEART_COMBINATION = new ArrayList<>(Arrays.asList(Q_H, J_H, TEN_H, NINE_H, EIGHT_H));

    private static final List<CardType> STRAIT_FLUSH_DIAMOND_FULL = new ArrayList<>(Arrays.asList(TWO_H, THREE_D, FOUR_D, FIVE_D, SIX_D, SEVEN_D, Q_H));
    private static final List<CardType> STRAIT_FLUSH_DIAMOND_COMBINATION = new ArrayList<>(Arrays.asList(SEVEN_D, SIX_D, FIVE_D, FOUR_D, THREE_D));

    private static final List<CardType> STRAIT_FLUSH_SPADE_FULL = new ArrayList<>(Arrays.asList(TWO_S, THREE_S, FOUR_S, FIVE_S, SIX_S, SEVEN_S, EIGHT_S));
    private static final List<CardType> STRAIT_FLUSH_SPADE_COMBINATION = new ArrayList<>(Arrays.asList(EIGHT_S, SEVEN_S, SIX_S, FIVE_S, FOUR_S));

    private static final List<CardType> STRAIT_FLUSH_FULL = new ArrayList<>(Arrays.asList(TWO_H, SEVEN_C, EIGHT_C, NINE_C, TEN_C, J_C, K_H));
    private static final List<CardType> STRAIT_FLUSH_COMBINATION = new ArrayList<>(Arrays.asList(J_C, TEN_C, NINE_C, EIGHT_C, SEVEN_C));

    private static final List<CardType> STRAIT_FLUSH_FULL_2 = new ArrayList<>(Arrays.asList(EIGHT_S, NINE_S, Q_S, TWO_D, THREE_C, J_S, TEN_S));
    private static final List<CardType> STRAIT_FLUSH_COMBINATION_2 = new ArrayList<>(Arrays.asList(Q_S, J_S, TEN_S, NINE_S, EIGHT_S));

    private static final List<CardType> STRAIT_FLUSH_WITH_ACE_FULL = new ArrayList<>(Arrays.asList(A_H, TWO_H, THREE_H, FOUR_H, FIVE_H, J_C, K_H));
    private static final List<CardType> STRAIT_FLUSH_WITH_ACE_COMBINATION = new ArrayList<>(Arrays.asList(A_H, FIVE_H, FOUR_H, THREE_H, TWO_H));

    private static final List<CardType> KARE_FULL = new ArrayList<>(Arrays.asList(NINE_C, FOUR_H, K_S, SIX_C, SIX_H, SIX_S, SIX_D));
    private static final List<CardType> KARE_COMBINATION = new ArrayList<>(Arrays.asList(SIX_C, SIX_H, SIX_S, SIX_D, K_S));

    private static final List<CardType> KARE_FULL2 = new ArrayList<>(Arrays.asList(A_C, A_D, A_S, A_H, TEN_C, SIX_D, K_H));
    private static final List<CardType> KARE_COMBINATION2 = new ArrayList<>(Arrays.asList(A_C, A_D, A_S, A_H, K_H));

    private static final List<CardType> KARE_FULL3 = new ArrayList<>(Arrays.asList(Q_C, FOUR_H, NINE_C, NINE_H, NINE_D, NINE_S, SIX_D));
    private static final List<CardType> KARE_COMBINATION3 = new ArrayList<>(Arrays.asList(NINE_C, NINE_H, NINE_D, NINE_S, Q_C));

    private static final List<CardType> KARE_FULL4 = new ArrayList<>(Arrays.asList(SEVEN_S, FOUR_H, SEVEN_C, SIX_C, SEVEN_D, SEVEN_H, A_D));
    private static final List<CardType> KARE_COMBINATION4 = new ArrayList<>(Arrays.asList(SEVEN_S, SEVEN_C, SEVEN_D, SEVEN_H, A_D));


    private static final List<CardType> FLUSH_HEART_FULL = new ArrayList<>(Arrays.asList(NINE_H, A_H, K_H, TWO_H, FIVE_H, FOUR_H, K_S));
    private static final List<CardType> FLUSH_HEART_COMBINATION = new ArrayList<>(Arrays.asList(A_H, K_H, NINE_H, FIVE_H, FOUR_H));

    private static final List<CardType> FLUSH_DIAMOND_FULL = new ArrayList<>(Arrays.asList(J_D, TWO_D, THREE_D, SEVEN_D, A_H, A_C, K_D));
    private static final List<CardType> FLUSH_DIAMOND_COMBINATION = new ArrayList<>(Arrays.asList(K_D, J_D, SEVEN_D, THREE_D, TWO_D));

    private static final List<CardType> FLUSH_SPADE_FULL = new ArrayList<>(Arrays.asList(K_S, Q_S, SEVEN_S, FOUR_S, FIVE_S, K_H, K_C));
    private static final List<CardType> FLUSH_SPADE_COMBINATION = new ArrayList<>(Arrays.asList(K_S, Q_S, SEVEN_S, FIVE_S, FOUR_S));

    private static final List<CardType> FLUSH_CLUB_FULL = new ArrayList<>(Arrays.asList(A_C, K_C, J_C, Q_C, NINE_C, TWO_C, THREE_C));
    private static final List<CardType> FLUSH_CLUB_COMBINATION = new ArrayList<>(Arrays.asList(A_C, K_C, Q_C, J_C, NINE_C));

    private static final List<CardType> FULL_HOUSE_FULL = new ArrayList<>(Arrays.asList(EIGHT_C, EIGHT_D, EIGHT_H, FIVE_D, FIVE_C, FIVE_H, FOUR_H));
    private static final List<CardType> FULL_HOUSE_COMBINATION = new ArrayList<>(Arrays.asList(FIVE_D, FIVE_C, EIGHT_C, EIGHT_D, EIGHT_H));

    private static final List<CardType> FULL_HOUSE_FULL2 = new ArrayList<>(Arrays.asList(A_D, A_C, SEVEN_D, A_H, K_C, K_H, FOUR_H));
    private static final List<CardType> FULL_HOUSE_COMBINATION2 = new ArrayList<>(Arrays.asList(K_C, K_H, A_D, A_C, A_H));

    private static final List<CardType> FULL_HOUSE_FULL3 = new ArrayList<>(Arrays.asList(EIGHT_C, EIGHT_D, Q_C, Q_H, Q_D, TEN_C, TEN_D));
    private static final List<CardType> FULL_HOUSE_COMBINATION3 = new ArrayList<>(Arrays.asList(TEN_C, TEN_D, Q_C, Q_H, Q_D));

    private static final List<CardType> FULL_HOUSE_FULL4 = new ArrayList<>(Arrays.asList(J_D, EIGHT_D, SEVEN_H, SEVEN_D, FIVE_C, J_C, J_H));
    private static final List<CardType> FULL_HOUSE_COMBINATION4 = new ArrayList<>(Arrays.asList(SEVEN_H, SEVEN_D, J_D, J_C, J_H));

    private static final List<CardType> STRAIT_FULL = new ArrayList<>(Arrays.asList(EIGHT_C, SEVEN_S, SIX_H, FIVE_D, THREE_D, FOUR_H, TWO_D));
    private static final List<CardType> STRAIT_COMBINATION = new ArrayList<>(Arrays.asList(EIGHT_C, SEVEN_S, SIX_H, FIVE_D, FOUR_H));


    private static final List<CardType> STRAIT_FULL_2 = new ArrayList<>(Arrays.asList(THREE_H, FIVE_C, SEVEN_C, EIGHT_C, NINE_D, TEN_H, J_H));
    private static final List<CardType> STRAIT_COMBINATION_2 = new ArrayList<>(Arrays.asList(J_H, TEN_H, NINE_D, EIGHT_C, SEVEN_C));


    private static final List<CardType> STRAIT_FULL_3 = new ArrayList<>(Arrays.asList(A_D, J_C, TEN_C, Q_C, K_C, J_H, J_D));
    private static final List<CardType> STRAIT_COMBINATION_3 = new ArrayList<>(Arrays.asList(A_D, K_C, Q_C, J_C, TEN_C));

    private static final List<CardType> STRAIT_FULL_4 = new ArrayList<>(Arrays.asList(SEVEN_C, EIGHT_C, NINE_C, TEN_H, J_H, Q_H, K_D));
    private static final List<CardType> STRAIT_COMBINATION_4 = new ArrayList<>(Arrays.asList(K_D, Q_H, J_H, TEN_H, NINE_C));

    private static final List<CardType> STRAIT_FULL_5 = new ArrayList<>(Arrays.asList(SEVEN_C, EIGHT_C, NINE_C, TEN_H, J_H, A_H, K_H));
    private static final List<CardType> STRAIT_COMBINATION_5 = new ArrayList<>(Arrays.asList(J_H, TEN_H, NINE_C, EIGHT_C, SEVEN_C));

    private static final List<CardType> STRAIT_FULL_6 = new ArrayList<>(Arrays.asList(TWO_H, SEVEN_C, EIGHT_C, NINE_C, TEN_H, J_H, K_H));
    private static final List<CardType> STRAIT_COMBINATION_6 = new ArrayList<>(Arrays.asList(J_H, TEN_H, NINE_C, EIGHT_C, SEVEN_C));

    private static final List<CardType> STRAIT_FULL_7 = new ArrayList<>(Arrays.asList(TWO_H, THREE_C, FOUR_S, FIVE_H, SIX_C, J_H, K_H));
    private static final List<CardType> STRAIT_COMBINATION_7 = new ArrayList<>(Arrays.asList(SIX_C, FIVE_H, FOUR_S, THREE_C, TWO_H));

    private static final List<CardType> STRAIT_FULL_8 = new ArrayList<>(Arrays.asList(A_C, TWO_C, THREE_C, FOUR_D, FIVE_D, J_H, K_H));
    private static final List<CardType> STRAIT_COMBINATION_8 = new ArrayList<>(Arrays.asList(A_C, TWO_C, THREE_C, FOUR_D, FIVE_D));

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
    public void testHeardFlushRoyal() {
        final Combination flashRoyal = checkHoldemCombinationService.findCombination(FLUSH_ROYAL_HEART_FULL);
        Assert.assertEquals(CombinationType.FLUSH_ROYAL, flashRoyal.getCombinationType());
        Assert.assertEquals(FLUSH_ROYAL_HEART_COMBINATION, flashRoyal.getCards());
    }

    @Test
    public void testSpadeFlushRoyal() {
        final Combination flashRoyal = checkHoldemCombinationService.findCombination(FLUSH_ROYAL_SPADE_FULL);
        Assert.assertEquals(CombinationType.FLUSH_ROYAL, flashRoyal.getCombinationType());
        Assert.assertEquals(FLUSH_ROYAL_SPADE_COMBINATION, flashRoyal.getCards());
    }

    @Test
    public void testDiamondFlushRoyal() {
        final Combination flashRoyal = checkHoldemCombinationService.findCombination(FLUSH_ROYAL_DIAMOND_FULL);
        Assert.assertEquals(CombinationType.FLUSH_ROYAL, flashRoyal.getCombinationType());
        Assert.assertEquals(FLUSH_ROYAL_DIAMOND_COMBINATION, flashRoyal.getCards());
    }

    @Test
    public void testClubFlushRoyal() {
        final Combination flashRoyal = checkHoldemCombinationService.findCombination(FLUSH_ROYAL_CLUB_FULL);
        Assert.assertEquals(CombinationType.FLUSH_ROYAL, flashRoyal.getCombinationType());
        Assert.assertEquals(FLUSH_ROYAL_CLUB_COMBINATION, flashRoyal.getCards());
    }

    @Test
    public void testStraitFlush() {
        final Combination straitFlush = checkHoldemCombinationService.findCombination(STRAIT_FLUSH_FULL);
        Assert.assertEquals(CombinationType.STRAIT_FLUSH, straitFlush.getCombinationType());
        Assert.assertEquals(STRAIT_FLUSH_COMBINATION, straitFlush.getCards());
    }

    @Test
    public void testStraitFlush2() {
        final Combination straitFlush = checkHoldemCombinationService.findCombination(STRAIT_FLUSH_FULL_2);
        Assert.assertEquals(CombinationType.STRAIT_FLUSH, straitFlush.getCombinationType());
        Assert.assertEquals(STRAIT_FLUSH_COMBINATION_2, straitFlush.getCards());
    }
    @Test
    public void testClubStraitFlush() {
        final Combination straitFlush = checkHoldemCombinationService.findCombination(STRAIT_FLUSH_CLUB_FULL);
        Assert.assertEquals(CombinationType.STRAIT_FLUSH, straitFlush.getCombinationType());
        Assert.assertEquals(STRAIT_FLUSH_CLUB_COMBINATION, straitFlush.getCards());
    }

    @Test
    public void testHeartStraitFlush() {
        final Combination straitFlush2 = checkHoldemCombinationService.findCombination(STRAIT_FLUSH_HEART_FULL);
        Assert.assertEquals(CombinationType.STRAIT_FLUSH, straitFlush2.getCombinationType());
        Assert.assertEquals(STRAIT_FLUSH_HEART_COMBINATION, straitFlush2.getCards());
    }

    @Test
    public void testDiamondStraitFlush() {
        final Combination straitFlush2 = checkHoldemCombinationService.findCombination(STRAIT_FLUSH_DIAMOND_FULL);
        Assert.assertEquals(CombinationType.STRAIT_FLUSH, straitFlush2.getCombinationType());
        Assert.assertEquals(STRAIT_FLUSH_DIAMOND_COMBINATION, straitFlush2.getCards());
    }

    @Test
    public void testSpadeStraitFlush() {
        final Combination straitFlush2 = checkHoldemCombinationService.findCombination(STRAIT_FLUSH_SPADE_FULL);
        Assert.assertEquals(CombinationType.STRAIT_FLUSH, straitFlush2.getCombinationType());
        Assert.assertEquals(STRAIT_FLUSH_SPADE_COMBINATION, straitFlush2.getCards());
    }

    @Test
    public void testStraitFlushWithAce() {
        final Combination straitFlushWithAce = checkHoldemCombinationService.findCombination(STRAIT_FLUSH_WITH_ACE_FULL);
        Assert.assertEquals(CombinationType.STRAIT_FLUSH, straitFlushWithAce.getCombinationType());
        Assert.assertEquals(STRAIT_FLUSH_WITH_ACE_COMBINATION, straitFlushWithAce.getCards());
    }


    @Test
    public void testKare() {
        final Combination kare = checkHoldemCombinationService.findCombination(KARE_FULL);
        Assert.assertEquals(CombinationType.KARE, kare.getCombinationType());
        Assert.assertEquals(KARE_COMBINATION, kare.getCards());
    }

    @Test
    public void testKare2() {
        final Combination kare = checkHoldemCombinationService.findCombination(KARE_FULL2);
        Assert.assertEquals(CombinationType.KARE, kare.getCombinationType());
        Assert.assertEquals(KARE_COMBINATION2, kare.getCards());
    }

    @Test
    public void testKare3() {
        final Combination kare = checkHoldemCombinationService.findCombination(KARE_FULL3);
        Assert.assertEquals(CombinationType.KARE, kare.getCombinationType());
        Assert.assertEquals(KARE_COMBINATION3, kare.getCards());
    }

    @Test
    public void testKare4() {
        final Combination kare = checkHoldemCombinationService.findCombination(KARE_FULL4);
        Assert.assertEquals(CombinationType.KARE, kare.getCombinationType());
        Assert.assertEquals(KARE_COMBINATION4, kare.getCards());
    }

    @Test
    public void testFullHouse() {
        final Combination fullHouse = checkHoldemCombinationService.findCombination(FULL_HOUSE_FULL);
        Assert.assertEquals(CombinationType.FULL_HOUSE, fullHouse.getCombinationType());
        Assert.assertEquals(FULL_HOUSE_COMBINATION, fullHouse.getCards());
    }

    @Test
    public void testFullHouse2() {
        final Combination fullHouse = checkHoldemCombinationService.findCombination(FULL_HOUSE_FULL2);
        Assert.assertEquals(CombinationType.FULL_HOUSE, fullHouse.getCombinationType());
        Assert.assertEquals(FULL_HOUSE_COMBINATION2, fullHouse.getCards());
    }

    @Test
    public void testFullHouse3() {
        final Combination fullHouse = checkHoldemCombinationService.findCombination(FULL_HOUSE_FULL3);
        Assert.assertEquals(CombinationType.FULL_HOUSE, fullHouse.getCombinationType());
        Assert.assertEquals(FULL_HOUSE_COMBINATION3, fullHouse.getCards());
    }

    @Test
    public void testFullHouse4() {
        final Combination fullHouse = checkHoldemCombinationService.findCombination(FULL_HOUSE_FULL4);
        Assert.assertEquals(CombinationType.FULL_HOUSE, fullHouse.getCombinationType());
        Assert.assertEquals(FULL_HOUSE_COMBINATION4, fullHouse.getCards());
    }

    @Test
    public void testHeartFlush() {
        final Combination flush = checkHoldemCombinationService.findCombination(FLUSH_HEART_FULL);
        Assert.assertEquals(CombinationType.FLUSH, flush.getCombinationType());
        Assert.assertEquals(FLUSH_HEART_COMBINATION, flush.getCards());
    }

    @Test
    public void testClubFlush() {
        final Combination flush = checkHoldemCombinationService.findCombination(FLUSH_CLUB_FULL);
        Assert.assertEquals(CombinationType.FLUSH, flush.getCombinationType());
        Assert.assertEquals(FLUSH_CLUB_COMBINATION, flush.getCards());
    }

    @Test
    public void testDiamondFlush() {
        final Combination flush = checkHoldemCombinationService.findCombination(FLUSH_DIAMOND_FULL);
        Assert.assertEquals(CombinationType.FLUSH, flush.getCombinationType());
        Assert.assertEquals(FLUSH_DIAMOND_COMBINATION, flush.getCards());
    }

    @Test
    public void testSpadeFlush() {
        final Combination flush = checkHoldemCombinationService.findCombination(FLUSH_SPADE_FULL);
        Assert.assertEquals(CombinationType.FLUSH, flush.getCombinationType());
        Assert.assertEquals(FLUSH_SPADE_COMBINATION, flush.getCards());
    }

    @Test
    public void testStrait() {
        final Combination straight = checkHoldemCombinationService.findCombination(STRAIT_FULL);
        Assert.assertEquals(CombinationType.STRAIT, straight.getCombinationType());
        Assert.assertEquals(STRAIT_COMBINATION, straight.getCards());
    }

    @Test
    public void testStrait2() {
        final Combination straight2 = checkHoldemCombinationService.findCombination(STRAIT_FULL_2);
        Assert.assertEquals(CombinationType.STRAIT, straight2.getCombinationType());
        Assert.assertEquals(STRAIT_COMBINATION_2, straight2.getCards());
    }

    @Test
    public void testStraight3() {
        Combination strait3 = checkHoldemCombinationService.findCombination(STRAIT_FULL_3);
        Assert.assertEquals(CombinationType.STRAIT, strait3.getCombinationType());
        Assert.assertEquals(STRAIT_COMBINATION_3, strait3.getCards());
    }

    @Test
    public void testStraight4() {
        Combination strait4 = checkHoldemCombinationService.findCombination(STRAIT_FULL_4);
        Assert.assertEquals(CombinationType.STRAIT, strait4.getCombinationType());
        Assert.assertEquals(STRAIT_COMBINATION_4, strait4.getCards());
    }

    @Test
    public void testStraight5() {
        Combination strait5 = checkHoldemCombinationService.findCombination(STRAIT_FULL_5);
        Assert.assertEquals(CombinationType.STRAIT, strait5.getCombinationType());
        Assert.assertEquals(STRAIT_COMBINATION_5, strait5.getCards());
    }

    @Test
    public void testStraight6() {
        Combination strait6 = checkHoldemCombinationService.findCombination(STRAIT_FULL_6);
        Assert.assertEquals(CombinationType.STRAIT, strait6.getCombinationType());
        Assert.assertEquals(STRAIT_COMBINATION_6, strait6.getCards());
    }

    @Test
    public void testStraight7() {
        Combination strait7 = checkHoldemCombinationService.findCombination(STRAIT_FULL_7);
        Assert.assertEquals(CombinationType.STRAIT, strait7.getCombinationType());
        Assert.assertEquals(STRAIT_COMBINATION_7, strait7.getCards());
    }

    @Test
    public void testStraight8() {
        Combination strait8 = checkHoldemCombinationService.findCombination(STRAIT_FULL_8);
        Assert.assertEquals(CombinationType.STRAIT, strait8.getCombinationType());
        Assert.assertEquals(STRAIT_COMBINATION_8, strait8.getCards());
    }

    @Test
    public void testThree() {
        final Combination three = checkHoldemCombinationService.findCombination(THREE_FULL);
        Assert.assertEquals(CombinationType.THREE, three.getCombinationType());
        Assert.assertEquals(THREE_COMBINATION, three.getCards());
    }

    @Test
    public void testThree2() {
        final Combination three_2 = checkHoldemCombinationService.findCombination(THREE_FULL_2);
        Assert.assertEquals(CombinationType.THREE, three_2.getCombinationType());
        Assert.assertEquals(THREE_COMBINATION_2, three_2.getCards());
    }

    @Test
    public void testThree3() {
        final Combination three_3 = checkHoldemCombinationService.findCombination(THREE_FULL_3);
        Assert.assertEquals(CombinationType.THREE, three_3.getCombinationType());
        Assert.assertEquals(THREE_COMBINATION_3, three_3.getCards());
    }

    @Test
    public void testTwoPair() {
        final Combination twoPair = checkHoldemCombinationService.findCombination(TWO_PAIR_FULL);
        Assert.assertEquals(CombinationType.TWO_PAIR, twoPair.getCombinationType());
        Assert.assertEquals(TWO_PAIR_COMBINATION, twoPair.getCards());
    }

    @Test
    public void testOnePair() {
        final Combination pair = checkHoldemCombinationService.findCombination(PAIR_FULL);
        Assert.assertEquals(CombinationType.PAIR, pair.getCombinationType());
        Assert.assertEquals(PAIR_FULL_COMBINATION, pair.getCards());
    }

    @Test
    public void testHighCard() {
        final Combination highCards = checkHoldemCombinationService.findCombination(HIGH_CARD_FULL);
        Assert.assertEquals(CombinationType.HIGH_CARD, highCards.getCombinationType());
        Assert.assertEquals(HIGH_COMBINATION, highCards.getCards());
    }

    @Test
    public void testHighCard2() {
        final Combination highCards_2 = checkHoldemCombinationService.findCombination(HIGH_CARD_FULL_2);
        Assert.assertEquals(CombinationType.HIGH_CARD, highCards_2.getCombinationType());
        Assert.assertEquals(HIGH_COMBINATION_2, highCards_2.getCards());
    }

}
