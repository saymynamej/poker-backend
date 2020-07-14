package ru.smn.poker.service;

import ru.smn.poker.dto.Combination;
import ru.smn.poker.dto.PlayerCombination;
import ru.smn.poker.enums.CardType;

import java.util.List;

public interface CombinationService {

    List<PlayerCombination> findWinners(List<PlayerCombination> playersAndCombinations);

    Combination findCombination(List<CardType> cards);
}
