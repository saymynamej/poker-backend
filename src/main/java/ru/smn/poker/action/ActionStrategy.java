package ru.smn.poker.action;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;

public interface ActionStrategy {

    void execute(
            PlayerEntity player,
            GameService gameService,
            ActionService actionService,
            CountAction countAction,
            TableSettings tableSettings
    );
}
