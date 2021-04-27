package ru.smn.poker.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.game.TableSettingsManager;
import ru.smn.poker.game.TableSettingsManagerFactory;
import ru.smn.poker.service.HandIdGenerator;
import ru.smn.poker.service.common.TableService;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum GameType implements TableSettingsManagerProducer {

    HOLDEM_FULL("HOLDEM_FULL") {
        @Override
        public TableSettingsManager produce(
                List<PlayerEntity> players,
                GameSettings gameSettings,
                HandIdGenerator handIdGenerator,
                TableService tableService
        ) {
            return TableSettingsManagerFactory.getManagerHoldemFull(
                    players,
                    gameSettings,
                    handIdGenerator,
                    tableService
            );
        }

        @Override
        public TableSettingsManager restore(
                List<PlayerEntity> players,
                GameSettings gameSettings,
                HandIdGenerator handIdGenerator,
                TableService tableService,
                TableSettings tableSettings
        ) {
            return TableSettingsManagerFactory.restoreManagerHoldem(
                    players,
                    gameSettings,
                    handIdGenerator,
                    tableService,
                    tableSettings
            );
        }
    },
    HOLDEM_HU("HOLDEM_HU") {
        @Override
        public TableSettingsManager produce(
                List<PlayerEntity> players,
                GameSettings gameSettings,
                HandIdGenerator handIdGenerator,
                TableService tableService
        ) {
            return TableSettingsManagerFactory.getManagerHoldemHU(
                    players,
                    gameSettings,
                    handIdGenerator,
                    tableService
            );
        }

        @Override
        public TableSettingsManager restore(
                List<PlayerEntity> players,
                GameSettings gameSettings,
                HandIdGenerator handIdGenerator,
                TableService tableService,
                TableSettings tableSettings
        ) {
            return TableSettingsManagerFactory.restoreManagerHoldemHU(
                    players,
                    gameSettings,
                    handIdGenerator,
                    tableService,
                    tableSettings
            );
        }
    };

    private final String gameName;

}

