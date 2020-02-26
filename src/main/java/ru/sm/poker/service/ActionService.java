package ru.sm.poker.service;

import ru.sm.poker.action.Action;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.Player;

public interface ActionService {

    void setAction(String playerName, Action action);

    void waitUntilPlayerWillHasAction(Player player, HoldemRoundSettingsDTO holdemRoundSettingsDTO);

    void changeStateType(String playerName);

}
