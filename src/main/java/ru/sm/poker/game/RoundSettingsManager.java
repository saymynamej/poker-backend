package ru.sm.poker.game;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;

public interface RoundSettingsManager {

    HoldemRoundSettingsDTO getPreflopSettings();

    HoldemRoundSettingsDTO getPostFlopSettings(long bank);

    HoldemRoundSettingsDTO getPostFlopSettingsWithTern(long bank);

    HoldemRoundSettingsDTO getPostFlopSettingsWithRiver(long bank);
}
