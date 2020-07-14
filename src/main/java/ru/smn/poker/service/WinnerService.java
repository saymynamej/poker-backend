package ru.smn.poker.service;

import ru.smn.poker.dto.HoldemRoundSettings;

public interface WinnerService {
    void sendPrizes(HoldemRoundSettings holdemRoundSettings);
}
