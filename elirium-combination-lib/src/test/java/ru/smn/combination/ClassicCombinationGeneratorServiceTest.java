package ru.smn.combination;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.smn.combination.data.CardSizeData;
import ru.smn.combination.data.Combination;

import static ru.smn.combination.data.CombinationType.*;

public class ClassicCombinationGeneratorServiceTest {
    private final CombinationGeneratorService combinationGeneratorService = new ClassicCombinationGeneratorService();
    private final CombinationService combinationService = new ClassicCombinationService();

    @Test
    public void shouldGenerateFlushRoyalCombination() {
        for (int i = 0; i < 100; i++) {
            final Combination flushRoyal = combinationGeneratorService.generate(FLUSH_ROYAL);
            final Combination combination = combinationService.findCombination(flushRoyal.getCards());
            Assertions.assertEquals(FLUSH_ROYAL, combination.getCombinationType());
            Assertions.assertEquals(CardSizeData.COMBINATION_SIZE, flushRoyal.getCards().size());
        }
    }

    @Test
    public void shouldGenerateStraightFlushCombination() {
        for (int i = 0; i < 100; i++) {
            final Combination straightFlush = combinationGeneratorService.generate(STRAIGHT_FLUSH);
            final Combination combination = combinationService.findCombination(straightFlush.getCards());
            Assertions.assertEquals(STRAIGHT_FLUSH, combination.getCombinationType());
            Assertions.assertEquals(CardSizeData.COMBINATION_SIZE, straightFlush.getCards().size());
        }
    }

    @Test
    public void shouldGenerateKareCombination() {
        for (int i = 0; i < 100; i++) {
            final Combination highCard = combinationGeneratorService.generate(KARE);
            final Combination combination = combinationService.findCombination(highCard.getCards());
            Assertions.assertEquals(KARE, combination.getCombinationType());
        }
    }

    @Test
    @Disabled
    public void shouldGenerateOnePairCombination() {
        for (int i = 0; i < 100; i++) {
            final Combination onePair = combinationGeneratorService.generate(ONE_PAIR);
            final Combination combination = combinationService.findCombination(onePair.getCards());
            Assertions.assertEquals(ONE_PAIR, combination.getCombinationType());
        }
    }
}
