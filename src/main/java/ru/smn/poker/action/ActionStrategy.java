package ru.smn.poker.action;

import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

public interface ActionStrategy {

    void execute(
            Player player,
            GameService gameService,
            ActionService actionService,
            CountAction countAction,
            HoldemRoundSettings holdemRoundSettings
    );
}
