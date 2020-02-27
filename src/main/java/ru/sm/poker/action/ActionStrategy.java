package ru.sm.poker.action;

import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

public interface ActionStrategy {

    void execute(
            Player player,
            GameService gameService,
            ActionService actionService,
            CountAction countAction,
            HoldemRoundSettings holdemRoundSettings
    );
}
