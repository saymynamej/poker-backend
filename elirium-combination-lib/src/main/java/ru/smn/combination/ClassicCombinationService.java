package ru.smn.combination;

import ru.smn.combination.assistant.SearchAssistant;
import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;
import ru.smn.combination.utils.ComparatorUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.combination.data.CardSizeData.COMBINATION_SIZE;

public class ClassicCombinationService implements CombinationService {

    @Override
    public Combination findCombination(List<CardType> cards) {
        if (cards.size() < COMBINATION_SIZE) {
            throw new RuntimeException("cards size cannot be less than " + COMBINATION_SIZE);
        }

        final List<CombinationType> sortedCombinationTypes = Arrays.stream(CombinationType.values())
                .sorted(ComparatorUtils.desc(CombinationType::getPower))
                .collect(Collectors.toList());

        for (CombinationType combinationType : sortedCombinationTypes) {
            final Combination combination = SearchAssistant.find(combinationType, new ArrayList<>(cards));
            if (!combination.isEmpty()){
                return combination;
            }
        }

        throw new RuntimeException("global error, could not found poker combination");
    }

}
