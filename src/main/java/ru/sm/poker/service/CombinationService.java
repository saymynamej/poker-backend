package ru.sm.poker.service;

import ru.sm.poker.dto.Combination;
import ru.sm.poker.dto.PlayerCombination;
import ru.sm.poker.enums.CardType;

import java.util.List;

public interface CombinationService {

    List<PlayerCombination> findWinners(List<PlayerCombination> playersAndCombinations);

    Combination findCombination(List<CardType> cards);
}
