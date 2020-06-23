package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.data.GameManagement;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.SeatManager;
import ru.sm.poker.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class HoldemQueueListener implements GameListener {
    private final GameManagement gameManagement;
    private final GameManager gameManager;
    private final SeatManager seatManager;
    private final OrderService orderService;
    private boolean isEnable = false;

    @Override
    public void listen() {
        isEnable = true;
        gameManagement.addListener(() -> {
            while (isEnable) {
                ThreadUtil.sleep(1);
                if (seatManager.getQueue().size() >= 4) {
                    gameManager.createGame(
                            extractQueue(),
                            GameType.HOLDEM_HU,
                            orderService,
                            true
                    );
                }
            }
        });
    }
    private List<PlayerDTO> extractQueue() {
        final List<PlayerDTO> players = new ArrayList<>();
        final Queue<PlayerDTO> queue = seatManager.getQueue();
        final int size = queue.size();
        for (int i = 0; i < size; i++) {
            players.add(queue.poll());
        }
        return players;
    }

    @Override
    public void stop() {
        isEnable = false;
    }
}
