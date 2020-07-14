package ru.smn.poker.service;

import ru.smn.poker.dto.HoldemRoundSettings;

public interface OrderService {
    boolean start(HoldemRoundSettings holdemRoundSettings);
}
