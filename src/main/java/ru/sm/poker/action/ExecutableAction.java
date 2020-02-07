package ru.sm.poker.action;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

public interface ExecutableAction extends CountAction {
    void doAction(
            HoldemRoundSettingsDTO holdemRoundSettingsDTO,
            PlayerDTO playerDTO,
            GameService gameService,
            ActionService actionService
    );
}
