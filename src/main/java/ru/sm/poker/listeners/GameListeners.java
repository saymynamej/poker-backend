package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.SeatManager;
import ru.sm.poker.util.PlayerUtil;
import ru.sm.poker.util.ThreadUtil;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
@RequiredArgsConstructor
@Slf4j
public class GameListeners {
    private final ExecutorService executorServiceForStart = Executors.newFixedThreadPool(1);
    private final ExecutorService executorServiceForGames = Executors.newFixedThreadPool(15);
    private final OrderService orderService;
    private final GameManager gameManager;
    private final SeatManager seatManager;
    private boolean isEnable = true;

    @PostConstruct
    public void init() {
        enableQueueListener();
        fillHoldemCashGames();
    }

    private void enableQueueListener() {
        executorServiceForStart.submit(() -> {
            while (isEnable) {
                ThreadUtil.sleep(1);
                if (seatManager.getQueue().size() >= 4) {
                    final Game game = gameManager.createGame(
                            extractQueue(),
                            GameType.HOLDEM_HU,
                            orderService
                    );
                    executorServiceForGames.submit(game::start);
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

    public void fillHoldemCashGames() {
        for (int i = 0; i < 1; i++) {
            Game game = gameManager.createGame(
                    Arrays.asList(
                            PlayerUtil.getDefaultBotForHoldem("1"),
                            PlayerUtil.getDefaultBotForHoldem("2")
                    ),
                    GameType.HOLDEM_FULL,
                    orderService);
            executorServiceForGames.submit(game::start);
        }
    }

}
