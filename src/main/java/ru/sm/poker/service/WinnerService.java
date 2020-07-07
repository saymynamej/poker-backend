package ru.sm.poker.service;

import ru.sm.poker.dto.HoldemRoundSettings;

public interface WinnerService {
    void sendPrizes(HoldemRoundSettings holdemRoundSettings);
}
