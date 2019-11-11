package ru.sm.poker.service;

import org.junit.Assert;
import org.junit.Test;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.Combination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//@SpringBootTest
//@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
public class CheckCombinationServiceTest {

    private final CheckCombinationService checkCombinationService = new CheckCombinationService();

    private static final List<CardType> FLUSH_ROUAL_FULL = new ArrayList<>(Arrays.asList(CardType.A_H, CardType.K_H, CardType.Q_H, CardType.J_H, CardType.TEN_H, CardType.FOUR_H, CardType.K_S));
    private static final List<CardType> FLUSH_ROYAL_COMBINATION = new ArrayList<>(Arrays.asList(CardType.A_H, CardType.K_H, CardType.Q_H, CardType.J_H, CardType.TEN_H));


    private static final List<CardType> STRAIT_FLUSH_FULL = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.SEVEN_C, CardType.TEN_C, CardType.J_C, CardType.NINE_C, CardType.FOUR_H, CardType.K_S));
    private static final List<CardType> STRAIT_FLUSH_COMBINATION = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.SEVEN_C, CardType.TEN_C, CardType.J_C, CardType.NINE_C));

    private static final List<CardType> KARE_FULL = new ArrayList<>(Arrays.asList(CardType.SIX_C, CardType.SIX_H, CardType.SIX_S, CardType.SIX_D, CardType.NINE_C, CardType.FOUR_H, CardType.K_S));
    private static final List<CardType> KARE_COMBINATION = new ArrayList<>(Arrays.asList(CardType.SIX_C, CardType.SIX_H, CardType.SIX_S, CardType.SIX_D,CardType.K_S));


    private static final List<CardType> FLUSH_FULL = new ArrayList<>(Arrays.asList(CardType.NINE_H, CardType.A_H, CardType.K_H, CardType.TWO_H, CardType.FIVE_H, CardType.FOUR_H, CardType.K_S));
    private static final List<CardType> FLUSH_COMBINATION = new ArrayList<>(Arrays.asList(CardType.NINE_H, CardType.A_H, CardType.K_H, CardType.FIVE_H, CardType.FOUR_H));


    private static final List<CardType> STRAIT_FULL = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.SEVEN_S, CardType.SIX_H, CardType.FIVE_D, CardType.THREE_D, CardType.FOUR_H));
    private static final List<CardType> STRAIT_COMBINATION = new ArrayList<>(Arrays.asList(CardType.EIGHT_C, CardType.SEVEN_S, CardType.SIX_H, CardType.FIVE_D, CardType.FOUR_H));




    @Test
    public void testFlushRoyal(){
       final Map<Combination, List<CardType>> flashRoyal = checkCombinationService.findCombination(FLUSH_ROUAL_FULL);
       Assert.assertEquals(flashRoyal.get(Combination.FLUSH_ROYAL), FLUSH_ROYAL_COMBINATION);
    }

    @Test
    public void testStraitFlash(){
        final Map<Combination, List<CardType>> straitFlush = checkCombinationService.findCombination(STRAIT_FLUSH_FULL);
        Assert.assertEquals(straitFlush.get(Combination.STRAIT_FLUSH), STRAIT_FLUSH_COMBINATION);
    }


    @Test
    public void testKare(){
        final Map<Combination, List<CardType>> kare = checkCombinationService.findCombination(KARE_FULL);
        Assert.assertEquals(kare.get(Combination.KARE), KARE_COMBINATION);
    }

    @Test
    public void testFlush(){
        final Map<Combination, List<CardType>> flush = checkCombinationService.findCombination(FLUSH_FULL);
        Assert.assertEquals(flush.get(Combination.FLUSH), FLUSH_COMBINATION);
    }

    @Test
    public void testStrait() {
        final Map<Combination, List<CardType>> straight = checkCombinationService.findCombination(STRAIT_FULL);
        Assert.assertEquals(straight.get(Combination.STRAIT), STRAIT_COMBINATION);
    }
}