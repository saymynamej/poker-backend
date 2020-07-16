package ru.smn.poker.game;

import ru.smn.poker.dto.RoundSettings;

public interface RoundSettingsManager {
    RoundSettings getSettings(RoundSettings prevSettings);
}
