package ru.sm.poker.game;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;

public interface Round {

    void startRound();

    void reloadRound();

    HoldemRoundSettingsDTO getHoldemRoundSettingsDTO();

}
