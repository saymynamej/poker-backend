package ru.sm.poker.service;

import org.apache.commons.lang3.tuple.Pair;
import ru.sm.poker.dto.CombinationDTO;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.model.Player;

import java.util.List;

public interface WinnerService {
    List<Pair<Player, CombinationDTO>> findWinners(List<Player> players, List<CardType> flop, CardType tern, CardType river);
}
