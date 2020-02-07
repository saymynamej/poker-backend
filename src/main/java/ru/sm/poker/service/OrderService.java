package ru.sm.poker.service;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;

public interface OrderService {
    boolean start(HoldemRoundSettingsDTO holdemRoundSettingsDTO);
}
