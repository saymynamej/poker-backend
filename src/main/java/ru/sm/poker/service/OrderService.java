package ru.sm.poker.service;

import ru.sm.poker.dto.RoundSettingsDTO;

public interface OrderService {
    void start(RoundSettingsDTO roundSettingsDTO);
}
