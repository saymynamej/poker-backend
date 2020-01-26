package ru.sm.poker.service;

import ru.sm.poker.action.Action;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.model.Player;

public interface ActionService {
    void setAction(String playerName, Action action);

    void waitUntilPlayerWillHasAction(Player player, HoldemRoundSettingsDTO holdemRoundSettingsDTO);

    void setUnSetAfkPlayer(String playerName);

}
