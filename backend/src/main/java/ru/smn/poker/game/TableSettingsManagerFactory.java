package ru.smn.poker.game;

import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.dto.HoldemTableSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.service.HandService;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class TableSettingsManagerFactory {

    private final static Random random = new SecureRandom();

    public static TableSettingsManager getManagerHoldemHU(
            List<PlayerEntity> players,
            GameSettings gameSettings,
            HandService handService
    ) {
        final Long tableId = gameSettings.getTableId();

        return new HUTableSettingsManager(
                random,
                handService.getNewHandId(tableId),
                players,
                gameSettings,
                new HoldemTableSettings()
        );
    }
}
