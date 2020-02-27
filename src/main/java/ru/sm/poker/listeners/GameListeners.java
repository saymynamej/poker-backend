package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.config.game.holdem.HoldemFullTableSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.game.Round;
import ru.sm.poker.game.holdem.HoldemGame;
import ru.sm.poker.game.holdem.HoldemRound;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.SeatManager;
import ru.sm.poker.service.WinnerService;
import ru.sm.poker.util.PlayerUtil;
import ru.sm.poker.util.ThreadUtil;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.sm.poker.util.GameUtil.getRandomGOTCityName;


@Component
@RequiredArgsConstructor
@Slf4j
public class GameListeners {

    private final ExecutorService executorServiceForStart = Executors.newFixedThreadPool(1);
    private final ExecutorService executorServiceForGames = Executors.newFixedThreadPool(15);
    private final OrderService orderService;
    private final GameManager gameManager;
    private final SeatManager seatManager;
    private final WinnerService winnerService;
    private boolean isEnable = true;

    @PostConstruct
    public void init() {
        enableHoldemCash();
        fillHoldemCashGames();
    }

    private void enableHoldemCash() {
        executorServiceForStart.submit(() -> {
            while (isEnable) {
                ThreadUtil.sleep(1);
                if (seatManager.getQueue().size() >= 4) {
                    final String randomGameName = getRandomGOTCityName();

                    final GameSettings gameSettings = new HoldemFullTableSettings(GameType.HOLDEM);

                    final Round round = new HoldemRound(
                            extractQueue(),
                            randomGameName,
                            orderService,
                            winnerService,
                            gameSettings.getStartSmallBlindBet(),
                            gameSettings.getStartBigBlindBet());

                    final Game holdemGame = new HoldemGame(
                            gameSettings,
                            round
                    );

                    gameManager.createNewGame(randomGameName, holdemGame);
                    executorServiceForGames.submit(holdemGame::start);
                }
            }
        });
    }

    private List<Player> extractQueue() {
        final List<Player> players = new ArrayList<>();
        final Queue<Player> queue = seatManager.getQueue();
        final int size = queue.size();
        for (int i = 0; i < size; i++) {
            players.add(queue.poll());
        }
        return players;
    }

    public void fillHoldemCashGames() {
        for (int i = 0; i < 10; i++) {
            final String randomGameName = getRandomGOTCityName();
            final GameSettings gameSettings = new HoldemFullTableSettings(GameType.HOLDEM);
            final List<Player> players = new ArrayList<>();

            final Round round = new HoldemRound(
                    players,
                    randomGameName,
                    orderService,
                    winnerService,
                    gameSettings.getStartSmallBlindBet(),
                    gameSettings.getStartBigBlindBet());

            final Game holdemGame = new HoldemGame(
                    gameSettings,
                    round
            );

            holdemGame.addPlayer(PlayerUtil.getDefaultBotForHoldem("3"));
            holdemGame.addPlayer(PlayerUtil.getDefaultBotForHoldem("2"));
            gameManager.createNewGame(randomGameName, holdemGame);
            executorServiceForGames.submit(holdemGame::start);
        }
    }

}
