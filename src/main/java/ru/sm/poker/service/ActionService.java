package ru.sm.poker.service;

import ru.sm.poker.action.Action;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;

public interface ActionService {

    void setAction(String playerName, Action action);

    void waitUntilPlayerWillHasAction(PlayerDTO player, HoldemRoundSettingsDTO holdemRoundSettings);

    void changeStateType(String playerName);

}
