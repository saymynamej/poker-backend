package ru.smn.poker.enums;

import ru.smn.poker.game.TableSettings;
import ru.smn.poker.game.TableSettingsManager;

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
            return FINISH;
        }

        @Override
        public TableSettings getCurrentSettings(TableSettingsManager settingsManager) {
            return settingsManager.getRiverSettings();
        }
    },

    FINISH {
        @Override
        public StageType getNextStage() {
            throw new RuntimeException("FINISH ACTION NOT HAS THE NEXT STAGE");
        }

        @Override
        public TableSettings getCurrentSettings(TableSettingsManager settingsManager) {
            throw new RuntimeException("FINISH ACTION NOT HAS THE CURRENT SETTINGS");
        }
    }
}
