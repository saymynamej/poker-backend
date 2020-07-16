package ru.smn.poker.service;

import ru.smn.poker.dto.RoundSettings;

public interface OrderService {
    boolean start(RoundSettings roundSettings);
}
