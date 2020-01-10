package ru.sm.poker.service;

import ru.sm.poker.model.Player;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.action.Action;

public interface ActionService {
    void setAction(String playerName, Action action);

    void setUnSetAfkPlayer(String name);

    void parseAction(Player player, RoundSettingsDTO roundSettingsDTO);

    void setActions(RoundSettingsDTO roundSettingsDTO);

    void setAction(Player player, RoundSettingsDTO roundSettingsDTO);

    void waitPlayerAction(Player player, RoundSettingsDTO roundSettingsDTO);

    void setLastBet(RoundSettingsDTO roundSettingsDTO, long count);

    void removeChipsPlayerAndAddToBank(Player player, long chips, RoundSettingsDTO roundSettingsDTO);

    void setPlayerWait(Player playerWait);
}
