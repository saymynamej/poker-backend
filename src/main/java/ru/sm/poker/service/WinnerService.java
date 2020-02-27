package ru.sm.poker.service;

import org.apache.commons.lang3.tuple.Pair;
import ru.sm.poker.dto.CombinationDTO;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.CardType;

import java.util.List;

public interface WinnerService {

    void sendPrizes(HoldemRoundSettings holdemRoundSettings, boolean isNotOpen);

    List<Pair<Player, CombinationDTO>> findCombinations(List<Player> players, List<CardType> deck);
}
