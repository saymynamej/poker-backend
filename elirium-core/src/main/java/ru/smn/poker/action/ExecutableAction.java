package ru.smn.poker.action;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

public interface ExecutableAction extends Action {
    void doAction(
            TableSettings tableSettings,
            PlayerEntity player,
            GameService gameService,
            ActionService actionService
    );
}
