package ru.smn.poker.game;

public interface TableSettingsManager {

    TableSettings getSettings();

    TableSettings getPreflopSettings();

    TableSettings getFlopSettings();

    TableSettings getTernSettings();

    TableSettings getRiverSettings();
}
