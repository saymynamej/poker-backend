package ru.sm.poker.model.action;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

public interface ExecutableAction extends Action {
    void doAction(RoundSettingsDTO roundSettingsDTOm, Player player, ActionService actionService);
}
