package ru.smn.poker.service;

import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.PlayerEntity;

import java.util.Queue;

public interface SeatManager {

    void joinInGame(String gameName, PlayerEntity player);

    void joinInQueue(PlayerEntity player);

    void leaveGame(String playerName, String gameName);

    Queue<PlayerEntity> getQueue();
}
