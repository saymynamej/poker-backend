package ru.sm.poker.service;

import ru.sm.poker.dto.PlayerDTO;

import java.util.Queue;

public interface SeatManager {

    void joinInGame(String gameName, PlayerDTO player);

    void joinInQueue(PlayerDTO player);

    void leaveGame(String playerName, String gameName);

    Queue<PlayerDTO> getQueue();
}
