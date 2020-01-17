package ru.sm.poker.action;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

public interface ExecutableAction extends CountAction {
    void doAction(RoundSettingsDTO roundSettingsDTO, Player player, GameService gameService, ActionService actionService);
}
