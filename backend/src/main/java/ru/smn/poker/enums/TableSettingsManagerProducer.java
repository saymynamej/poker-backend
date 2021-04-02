package ru.smn.poker.enums;

import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.game.TableSettingsManager;
import ru.smn.poker.service.HandIdGenerator;
import ru.smn.poker.service.common.TableService;

import java.util.List;

public interface TableSettingsManagerProducer {
    TableSettingsManager produce(
            List<PlayerEntity> players,
            GameSettings gameSettings,
            HandIdGenerator handIdGenerator,
            TableService tableService
    );

    TableSettingsManager restore(
            List<PlayerEntity> players,
            GameSettings gameSettings,
            HandIdGenerator handIdGenerator,
            TableService tableService,
            TableSettings tableSettings
    );
}

