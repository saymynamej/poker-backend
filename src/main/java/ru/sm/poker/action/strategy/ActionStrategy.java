package ru.sm.poker.action.strategy;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.service.ActionService;

public interface ActionStrategy {
    void execute(Player player, ActionService actionService, CountAction countAction, RoundSettingsDTO roundSettingsDTO);
}
