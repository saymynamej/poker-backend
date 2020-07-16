package ru.smn.poker.game;

public interface RoundSettingsManager {
    RoundSettings getSettings(RoundSettings prevSettings);
}
