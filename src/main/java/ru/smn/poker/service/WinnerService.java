package ru.smn.poker.service;

import ru.smn.poker.game.RoundSettings;

public interface WinnerService {
    void sendPrizes(RoundSettings roundSettings);
}
