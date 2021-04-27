package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.service.QueueService;
import ru.smn.poker.service.SeatManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class SimpleQueueService implements QueueService {
    private final SeatManager seatManager;

    public List<PlayerEntity> extractQueue() {
        final List<PlayerEntity> players = new ArrayList<>();
        final Queue<PlayerEntity> queue = seatManager.getQueue();
        final int size = queue.size();
        for (int i = 0; i < size; i++) {
            players.add(queue.poll());
        }
        return players;
    }
}
