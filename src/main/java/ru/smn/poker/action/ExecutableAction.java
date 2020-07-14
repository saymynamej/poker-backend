package ru.smn.poker.action;

import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

public interface ExecutableAction extends CountAction {
    void doAction(
            HoldemRoundSettings holdemRoundSettings,
            Player player,
            GameService gameService,
            ActionService actionService
    );
}
