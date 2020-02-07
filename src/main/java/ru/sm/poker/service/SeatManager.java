package ru.sm.poker.service;

import ru.sm.poker.dto.PlayerDTO;

import java.util.Queue;

public interface SeatManager {

    void joinInGame(String gameName, PlayerDTO playerDTO);

    void joinInQueue(PlayerDTO playerDTO);

    void leaveGame(String playerName, String gameName);

    Queue<PlayerDTO> getQueue();
}
