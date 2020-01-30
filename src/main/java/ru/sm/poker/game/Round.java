package ru.sm.poker.game;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;

import java.util.List;

public interface Round {

    void startRound();

    void reloadRound();

    HoldemRoundSettingsDTO getHoldemRoundSettingsDTO();

    List<PlayerDTO> getPlayers();

}
