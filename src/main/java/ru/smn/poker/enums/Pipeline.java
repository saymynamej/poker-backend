package ru.smn.poker.enums;

import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.game.RoundSettingsManager;

public interface Pipeline {
    StageType getNextStage();

    RoundSettings getCurrentSettings(RoundSettingsManager settingsManager);
}
