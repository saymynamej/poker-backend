package ru.smn.poker.enums;

import lombok.RequiredArgsConstructor;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.game.TableSettingsManager;

@RequiredArgsConstructor
public enum StageType implements Pipeline {

    PREFLOP {
        @Override
        public StageType getNextStage() {
            return FLOP;
        }

        @Override
        public TableSettings getCurrentSettings(TableSettingsManager settingsManager) {
            return settingsManager.getPreflopSettings();
        }
    },
    FLOP {
        @Override
        public StageType getNextStage() {
            return TERN;
        }

        @Override
        public TableSettings getCurrentSettings(TableSettingsManager settingsManager) {
            return settingsManager.getFlopSettings();
        }
    },
    TERN {
        @Override
        public StageType getNextStage() {
            return RIVER;
        }

        @Override
        public TableSettings getCurrentSettings(TableSettingsManager settingsManager) {
            return settingsManager.getTernSettings();
        }
    },
    RIVER {
        @Override
        public StageType getNextStage() {
            return PREFLOP;
        }

        @Override
        public TableSettings getCurrentSettings(TableSettingsManager settingsManager) {
            return settingsManager.getRiverSettings();
        }
    }
}
