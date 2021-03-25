package ru.smn.poker.game;

import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.service.common.HandService;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class HoldemRoundSettingsManagerFactory {

    private final static Random random = new SecureRandom();

    public static TableSettingsManager getManager(
            List<PlayerEntity> players,
            GameSettings gameSettings,
            HandService handService
    ) {
        return new HoldemTableSettingsManagerHU(
                random,
                handService.getNewHandId(),
                players,
                gameSettings
        );
    }
}
