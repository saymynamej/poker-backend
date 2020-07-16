package ru.smn.poker.service;

import ru.smn.poker.dto.RoundSettings;

public interface WinnerService {
    void sendPrizes(RoundSettings roundSettings);
}
