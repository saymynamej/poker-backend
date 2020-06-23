package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.data.GamePool;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.SeatManager;
import ru.sm.poker.util.ThreadUtil;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


@Component
@RequiredArgsConstructor
@Slf4j
public class GameListeners {
    private final OrderService orderService;
    private final GameManager gameManager;
    private final SeatManager seatManager;
    private final GamePool gamePool;
    private boolean isEnable = true;

    @PostConstruct
    public void init() {
        enableQueueListener();
    }

    private void enableQueueListener() {
        gamePool.addListener(() -> {
            while (isEnable) {
                ThreadUtil.sleep(1);
                if (seatManager.getQueue().size() >= 4) {
                    final Game game = gameManager.createGame(
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
}
