package ru.smn.poker.service;

import ru.smn.poker.game.TableSettings;

public interface PrizeService {
    void sendPrizes(TableSettings tableSettings);
}
