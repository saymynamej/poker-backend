package ru.sm.poker.game;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;

public interface RoundSettingsManager {

    HoldemRoundSettingsDTO getSettings(HoldemRoundSettingsDTO prevSettings);

//    HoldemRoundSettingsDTO getPreflopSettings();
//
//    HoldemRoundSettingsDTO getPostFlopSettings(long bank, Map<PlayerDTO, List<Action>> prevHistory);
//
//    HoldemRoundSettingsDTO getPostFlopSettingsWithTern(long bank, Map<PlayerDTO, List<Action>> fullHistory);
//
//    HoldemRoundSettingsDTO getPostFlopSettingsWithRiver(long bank, Map<PlayerDTO, List<Action>> fullHistory);
}
