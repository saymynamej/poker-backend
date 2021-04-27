package ru.smn.poker.service;

import ru.smn.poker.action.Action;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;

public interface ActionService {

    void setAction(String playerName, Action action);

    void waitPlayerAction(PlayerEntity player, TableSettings tableSettings);

    void changeStateType(String playerName);

}
