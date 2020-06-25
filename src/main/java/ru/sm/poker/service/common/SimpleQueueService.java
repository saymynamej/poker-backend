package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.service.QueueService;
import ru.sm.poker.service.SeatManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class SimpleQueueService implements QueueService {
    private final SeatManager seatManager;

    public List<PlayerDTO> extractQueue() {
        final List<PlayerDTO> players = new ArrayList<>();
        final Queue<PlayerDTO> queue = seatManager.getQueue();
        final int size = queue.size();
        for (int i = 0; i < size; i++) {
            players.add(queue.poll());
        }
        return players;
    }
}
