package ru.sm.poker.game;

import ru.sm.poker.action.Action;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;

import java.util.List;
import java.util.Map;

public interface RoundSettingsManager {

    HoldemRoundSettings getPreflopSettings();

    HoldemRoundSettings getPostFlopSettings(long bank, Map<Player, List<Action>> prevHistory);

    HoldemRoundSettings getPostFlopSettingsWithTern(long bank, Map<Player, List<Action>> fullHistory);

    HoldemRoundSettings getPostFlopSettingsWithRiver(long bank, Map<Player, List<Action>> fullHistory);
}
