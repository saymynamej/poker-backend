package ru.sm.poker.game;

import ru.sm.poker.dto.RoundSettingsDTO;

public interface RoundSettingsManager {

    RoundSettingsDTO getPreflopSettings();

    RoundSettingsDTO getPostFlopSettings(long bank);

    RoundSettingsDTO getPostFlopSettingsWithTern(long bank);

    RoundSettingsDTO getPostFlopSettingsWithRiver(long bank);
}
