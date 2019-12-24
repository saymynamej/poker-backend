package ru.sm.poker.service;

import ru.sm.poker.model.Player;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.action.Action;

public interface ActionService {
    void setAction(String playerName, Action action);
    void parseAction(Player player, RoundSettingsDTO roundSettingsDTO);
    void setActions(RoundSettingsDTO roundSettingsDTO);
}
