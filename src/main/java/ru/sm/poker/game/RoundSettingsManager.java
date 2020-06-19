package ru.sm.poker.game;

import ru.sm.poker.action.Action;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;

import java.util.List;
import java.util.Map;

public interface RoundSettingsManager {

    HoldemRoundSettingsDTO getPreflopSettings();

    HoldemRoundSettingsDTO getPostFlopSettings(long bank, Map<PlayerDTO, List<Action>> prevHistory);

    HoldemRoundSettingsDTO getPostFlopSettingsWithTern(long bank, Map<PlayerDTO, List<Action>> fullHistory);

    HoldemRoundSettingsDTO getPostFlopSettingsWithRiver(long bank, Map<PlayerDTO, List<Action>> fullHistory);
}
