package ru.smn.combination;

import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;
import ru.smn.combination.strategy.generator.GeneratorAssistant;

public class ClassicCombinationGeneratorService implements CombinationGeneratorService {

    @Override
    public Combination generate(CombinationType combinationType) {
        return GeneratorAssistant.generate(combinationType);
    }
}
