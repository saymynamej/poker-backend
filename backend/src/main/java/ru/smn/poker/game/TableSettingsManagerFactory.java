package ru.smn.poker.game;

import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.dto.ClassicTableSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.service.HandIdGenerator;
import ru.smn.poker.service.common.TableService;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class TableSettingsManagerFactory {

    private final static Random random = new SecureRandom();

    public static TableSettingsManager getManagerHoldemFull(
            List<PlayerEntity> players,
            GameSettings gameSettings,
            HandIdGenerator handIdGenerator,
            TableService tableService
    ) {

        return new FullTableSettingManager(
                random,
                players,
                gameSettings,
                new ClassicTableSettings(),
                handIdGenerator,
                tableService
        );
    }

    public static TableSettingsManager getManagerHoldemHU(
            List<PlayerEntity> players,
            GameSettings gameSettings,
            HandIdGenerator handIdGenerator,
            TableService tableService
    ) {

        return new HUTableSettingsManager(
                random,
                players,
                gameSettings,
                new ClassicTableSettings(),
                handIdGenerator,
                tableService
        );
    }
}
