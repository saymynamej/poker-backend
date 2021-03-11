package ru.smn.poker.enums;

import ru.smn.poker.game.TableSettings;
import ru.smn.poker.game.TableSettingsManager;

public interface Pipeline {
    StageType getNextStage();

    TableSettings getCurrentSettings(TableSettingsManager settingsManager);
}
