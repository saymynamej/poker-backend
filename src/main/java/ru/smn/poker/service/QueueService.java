package ru.smn.poker.service;

import ru.smn.poker.dto.Player;

import java.util.List;

public interface QueueService {
    List<Player> extractQueue();
}
