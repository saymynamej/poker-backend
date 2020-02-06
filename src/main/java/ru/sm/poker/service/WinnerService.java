package ru.sm.poker.service;

import org.apache.commons.lang3.tuple.Pair;
import ru.sm.poker.dto.CombinationDTO;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.CardType;

import java.util.List;

public interface WinnerService {
    void sendPrizes(HoldemRoundSettingsDTO holdemRoundSettingsDTO);
    List<Pair<PlayerDTO, CombinationDTO>> findWinners(List<PlayerDTO> playerDTOS, List<CardType> deck);
}
