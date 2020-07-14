package ru.smn.poker.service;

import ru.smn.poker.action.Action;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;

public interface ActionService {

    void setAction(String playerName, Action action);

    void waitUntilPlayerWillHasAction(Player player, HoldemRoundSettings holdemRoundSettings);

    void changeStateType(String playerName);

}
