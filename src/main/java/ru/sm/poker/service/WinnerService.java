package ru.sm.poker.service;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;

public interface WinnerService {
    void sendPrizes(HoldemRoundSettingsDTO holdemRoundSettings);
}
