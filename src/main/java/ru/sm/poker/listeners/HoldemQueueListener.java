package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.game.Game;
import ru.sm.poker.service.GameManagementService;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.QueueService;
import ru.sm.poker.service.SeatManager;
import ru.sm.poker.util.ThreadUtil;

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
                    final Game game = gameManagementService.createGame(
                            queueService.extractQueue(),
                            GameType.HOLDEM_HU,
                            orderService
                    );
                    gameManagementService.saveGame(game);
                    gameManagementService.startGame(game);
                }
            }
        });
    }

    @Override
    public void stop() {
        isEnable = false;
    }
}
