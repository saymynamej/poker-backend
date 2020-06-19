package ru.sm.poker.action;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

public interface ActionStrategy {

    void execute(
            PlayerDTO player,
            GameService gameService,
            ActionService actionService,
            CountAction countAction,
            HoldemRoundSettingsDTO holdemRoundSettings
    );
}
