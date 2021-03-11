package ru.smn.poker.enums;

import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.game.RoundSettingsManager;

public enum StageType implements Pipeline {
    PREFLOP {
        @Override
        public StageType getNextStage() {
            return FLOP;
        }

        @Override
        public RoundSettings getCurrentSettings(RoundSettingsManager settingsManager) {
            return null;
        }
    },
    FLOP {
        @Override
        public StageType getNextStage() {
            return TERN;
        }

        @Override
        public RoundSettings getCurrentSettings(RoundSettingsManager settingsManager) {
            return null;
        }
    },
    TERN {
        @Override
        public StageType getNextStage() {
            return RIVER;
        }

        @Override
        public RoundSettings getCurrentSettings(RoundSettingsManager settingsManager) {
            return null;
        }
    },
    RIVER {
        @Override
        public StageType getNextStage() {
            return FINISH;
        }

        @Override
        public RoundSettings getCurrentSettings(RoundSettingsManager settingsManager) {
            return null;
        }
    },

    FINISH {
        @Override
        public StageType getNextStage() {
            throw new RuntimeException("FINISH ACTION NOT HAS THE NEXT STAGE");
        }

        @Override
        public RoundSettings getCurrentSettings(RoundSettingsManager settingsManager) {
            throw new RuntimeException("FINISH ACTION NOT HAS THE CURRENT SETTINGS");
        }
    }
}
