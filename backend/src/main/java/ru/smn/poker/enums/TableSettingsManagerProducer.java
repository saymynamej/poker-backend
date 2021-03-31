package ru.smn.poker.enums;

import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettingsManager;
import ru.smn.poker.service.HandIdGenerator;
import ru.smn.poker.service.common.TableService;

import java.util.List;

public interface TableSettingsManagerProducer {
    TableSettingsManager produceManager(
            List<PlayerEntity> players,
            GameSettings gameSettings,
            HandIdGenerator handIdGenerator,
            TableService tableService
    );
}

