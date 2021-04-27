package ru.smn.poker.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.service.GameTableCreator;
import ru.smn.poker.service.QueueService;
import ru.smn.poker.service.SeatManager;

@Service
@RequiredArgsConstructor
public class HoldemQueueListener implements GameListener {
    private final GameTableCreator gameTableCreator;
    private final SeatManager seatManager;
    private final QueueService queueService;
    private boolean isEnable = false;

    @Override
    public void listen() {
        isEnable = true;
    }

    @Override
    public void stop() {
        isEnable = false;
    }
}
