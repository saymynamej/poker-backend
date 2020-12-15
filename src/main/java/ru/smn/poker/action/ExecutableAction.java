package ru.smn.poker.action;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;

public interface ExecutableAction extends CountAction {
    void doAction(
            RoundSettings roundSettings,
            PlayerEntity player,
            GameService gameService,
            ActionService actionService
    );
}
