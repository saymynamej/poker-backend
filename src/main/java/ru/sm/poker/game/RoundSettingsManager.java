package ru.sm.poker.game;

import ru.sm.poker.dto.HoldemRoundSettings;

public interface RoundSettingsManager {
    HoldemRoundSettings getSettings(HoldemRoundSettings prevSettings);
}
