package ru.sm.poker.service;

import ru.sm.poker.dto.PlayerDTO;

import java.util.List;

public interface QueueService {
    List<PlayerDTO> extractQueue();
}
