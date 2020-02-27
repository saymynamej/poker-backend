package ru.sm.poker.service;

import ru.sm.poker.action.Action;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;

public interface ActionService {

    void setAction(String playerName, Action action);

    void waitUntilPlayerWillHasAction(Player player, HoldemRoundSettings holdemRoundSettings);

    void changeStateType(String playerName);

}
