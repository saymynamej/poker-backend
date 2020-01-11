package ru.sm.poker.action;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

public interface ExecutableAction extends CountAction {
    void doAction(RoundSettingsDTO roundSettingsDTOm, Player player, ActionService actionService);
}
