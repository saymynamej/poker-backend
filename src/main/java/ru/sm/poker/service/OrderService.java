package ru.sm.poker.service;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;

public interface OrderService {
    void start(HoldemRoundSettingsDTO holdemRoundSettingsDTO);
}
