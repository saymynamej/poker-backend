package ru.smn.poker.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.service.GameManagementService;
import ru.smn.poker.service.OrderService;
import ru.smn.poker.service.QueueService;
import ru.smn.poker.service.SeatManager;
import ru.smn.poker.util.ThreadUtil;

@Service
@RequiredArgsConstructor
public class HoldemQueueListener implements GameListener {
    private final GameManagementService gameManagementService;
    private final SeatManager seatManager;
    private final OrderService orderService;
    private final QueueService queueService;
    private boolean isEnable = false;

    @Override
    public void listen() {
        isEnable = true;
        gameManagementService.addListener(() -> {
            while (isEnable) {
                ThreadUtil.sleep(1);
                if (seatManager.getQueue().size() >= 4) {
                    gameManagementService.createGame(
                            queueService.extractQueue(),
                            GameType.HOLDEM_HU,
                            orderService
                    );
                }
            }
        });
    }

    @Override
    public void stop() {
        isEnable = false;
    }
}
