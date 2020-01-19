package ru.sm.poker.service;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;

import java.util.List;

public interface OrderService {
    void start(RoundSettingsDTO roundSettingsDTO);
}
