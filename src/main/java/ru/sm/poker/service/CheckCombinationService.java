package ru.sm.poker.service;

import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.Combination;

import java.util.List;
import java.util.Map;

public interface CheckCombinationService {

    Map<Combination, List<CardType>> findCombination(List<CardType> cards);
}
