package ru.smn.poker.service;

import ru.smn.poker.dto.Player;

import java.util.Queue;

public interface SeatManager {

    void joinInGame(String gameName, Player player);

    void joinInQueue(Player player);

    void leaveGame(String playerName, String gameName);

    Queue<Player> getQueue();
}
