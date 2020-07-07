package ru.sm.poker.action;

import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

public interface ExecutableAction extends CountAction {
    void doAction(
            HoldemRoundSettings holdemRoundSettings,
            Player player,
            GameService gameService,
            ActionService actionService
    );
}
