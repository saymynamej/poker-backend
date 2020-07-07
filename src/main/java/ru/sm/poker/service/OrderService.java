package ru.sm.poker.service;

import ru.sm.poker.dto.HoldemRoundSettings;

public interface OrderService {
    boolean start(HoldemRoundSettings holdemRoundSettings);
}
