package ru.sm.poker.service;

import org.apache.commons.lang3.tuple.Pair;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;

import java.util.List;

public interface CheckCombinationService {

    Pair<CombinationType, List<CardType>> findCombination(List<CardType> cards);
}
