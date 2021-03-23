package ru.smn.poker.game;

public interface TableSettingsManager {
    void commit(TableSettings tableSettings);

    TableSettings getSettings();

    TableSettings getPreflopSettings();

    TableSettings getFlopSettings();

    TableSettings getTernSettings();

    TableSettings getRiverSettings();
}
