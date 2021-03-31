package ru.smn.poker.enums;

import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettingsManager;
import ru.smn.poker.service.HandService;

import java.util.List;

public interface TableSettingsManagerProducer {
    TableSettingsManager produceManager(
            List<PlayerEntity> players,
            GameSettings gameSettings,
            HandService handService
    );
}

