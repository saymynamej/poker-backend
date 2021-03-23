package ru.smn.poker.action;

import ru.smn.poker.entities.PlayerEntity;

import java.util.List;

public interface NextPlayerSelector {

    PlayerEntity getPlayerForAction(List<PlayerEntity> sortedPlayers, PlayerEntity previousActivePlayer);
}
