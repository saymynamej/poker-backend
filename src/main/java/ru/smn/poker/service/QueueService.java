package ru.smn.poker.service;

import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.PlayerEntity;

import java.util.List;

public interface QueueService {
    List<PlayerEntity> extractQueue();
}
