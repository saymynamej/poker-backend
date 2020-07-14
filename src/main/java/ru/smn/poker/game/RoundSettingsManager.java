package ru.smn.poker.game;

import ru.smn.poker.dto.HoldemRoundSettings;

public interface RoundSettingsManager {
    HoldemRoundSettings getSettings(HoldemRoundSettings prevSettings);
}
