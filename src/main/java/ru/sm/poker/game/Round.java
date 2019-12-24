package ru.sm.poker.game;

import ru.sm.poker.dto.RoundSettingsDTO;

public interface Round {

    void startRound();

    void reloadRound();

    RoundSettingsDTO getRoundSettingsDTO();

}
