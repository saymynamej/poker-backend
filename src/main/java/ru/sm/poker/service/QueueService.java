package ru.sm.poker.service;

import ru.sm.poker.dto.Player;

import java.util.List;

public interface QueueService {
    List<Player> extractQueue();
}
