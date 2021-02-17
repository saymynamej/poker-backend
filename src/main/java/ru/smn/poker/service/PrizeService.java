package ru.smn.poker.service;

import ru.smn.poker.game.RoundSettings;

public interface PrizeService {
    void sendPrizes(RoundSettings roundSettings);
}
