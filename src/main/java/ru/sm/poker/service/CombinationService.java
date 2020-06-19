package ru.sm.poker.service;

import ru.sm.poker.dto.CombinationDTO;
import ru.sm.poker.dto.PlayerCombinationDTO;
import ru.sm.poker.enums.CardType;

import java.util.List;

public interface CombinationService {

    List<PlayerCombinationDTO> findMoreStrongerCombinations(List<PlayerCombinationDTO> playersAndCombinations);

    CombinationDTO findCombination(List<CardType> cards);
}
