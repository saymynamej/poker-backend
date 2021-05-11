package ru.smn.combination;

import ru.smn.combination.assistant.GeneratorAssistant;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

public class ClassicCombinationGeneratorService implements CombinationGeneratorService {

    @Override
    public Combination generate(CombinationType combinationType) {
        return GeneratorAssistant.generate(combinationType);
    }
}
