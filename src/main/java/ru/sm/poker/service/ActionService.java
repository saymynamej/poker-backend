package ru.sm.poker.service;

import ru.sm.poker.action.Action;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;

import java.security.Principal;

public interface ActionService {
    void setAction(String playerName, Action action);

    void waitUntilPlayerWillHasAction(Player player, RoundSettingsDTO roundSettingsDTO);

    void setUnSetAfkPlayer(String playerName);

}
