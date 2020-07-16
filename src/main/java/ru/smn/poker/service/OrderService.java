package ru.smn.poker.service;

import ru.smn.poker.game.RoundSettings;

public interface OrderService {
    boolean start(RoundSettings roundSettings);
}
