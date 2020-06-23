package ru.sm.poker.game;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;

public interface RoundSettingsManager {
    HoldemRoundSettingsDTO getSettings(HoldemRoundSettingsDTO prevSettings);
}
