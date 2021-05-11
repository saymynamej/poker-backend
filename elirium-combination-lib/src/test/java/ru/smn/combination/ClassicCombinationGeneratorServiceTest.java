package ru.smn.combination;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.smn.combination.data.Combination;

import static ru.smn.combination.data.CombinationType.HIGH_CARD;

public class ClassicCombinationGeneratorServiceTest {
    private final CombinationGeneratorService combinationGeneratorService = new ClassicCombinationGeneratorService();
    private final CombinationService combinationService = new ClassicCombinationService();

    @Test
    public void shouldGenerateHighCardCombination() {
        for (int i = 0; i < 100; i++) {
            final Combination highCard = combinationGeneratorService.generate(HIGH_CARD);
            final Combination combination = combinationService.findCombination(highCard.getCards());
            Assertions.assertEquals(HIGH_CARD, combination.getCombinationType());
        }
    }
}
