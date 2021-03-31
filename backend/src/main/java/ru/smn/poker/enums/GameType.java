package ru.smn.poker.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettingsManager;
import ru.smn.poker.game.TableSettingsManagerFactory;
import ru.smn.poker.service.HandIdGenerator;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum GameType implements TableSettingsManagerProducer {

    HOLDEM_FULL("HOLDEM_FULL") {
        @Override
        public TableSettingsManager produceManager(
                List<PlayerEntity> players,
                GameSettings gameSettings,
                HandIdGenerator handIdGenerator
        ) {
            return TableSettingsManagerFactory.getManagerHoldemFull(
                    players,
                    gameSettings,
                    handIdGenerator
            );
        }
    },
    HOLDEM_HU("HOLDEM_HU") {
        @Override
        public TableSettingsManager produceManager(
                List<PlayerEntity> players,
                GameSettings gameSettings,
                HandIdGenerator handIdGenerator
        ) {
            return TableSettingsManagerFactory.getManagerHoldemHU(
                    players,
                    gameSettings,
                    handIdGenerator
            );
        }
    };

    private final String gameName;

}

