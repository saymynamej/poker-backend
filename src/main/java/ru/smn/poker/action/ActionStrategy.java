package ru.smn.poker.action;

import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

public interface ActionStrategy {

    void execute(
            PlayerEntity player,
            GameService gameService,
            ActionService actionService,
            CountAction countAction,
            RoundSettings roundSettings
    );
}
