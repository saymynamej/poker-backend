package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.config.game.holdem.HoldemFullTableSettings;
import ru.sm.poker.config.game.holdem.HoldemHUTableSettings;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.game.Round;
import ru.sm.poker.game.holdem.HoldemGame;
import ru.sm.poker.game.holdem.HoldemRound;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.SeatManager;
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
                            gameSettings.getStartSmallBlindBet(),
                            gameSettings.getStartBigBlindBet());

                    final Game holdemGame = new HoldemGame(
                            gameSettings,
                            round
                    );

                    gameManager.createGame(randomGameName, holdemGame);
                    executorServiceForGames.submit(holdemGame::start);
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
        for (int i = 0; i < 5; i++) {
            createGame(new HoldemHUTableSettings(GameType.HOLDEM));
            createGame2(new HoldemFullTableSettings(GameType.HOLDEM));
        }
    }

    private void createGame2(GameSettings gameSettings) {
        final String randomGameName = getRandomGOTCityName();
        final List<PlayerDTO> players = new ArrayList<>();
        final Round round = new HoldemRound(
                players,
                randomGameName,
                orderService,
                gameSettings.getStartSmallBlindBet(),
                gameSettings.getStartBigBlindBet());
        final Game holdemGame = new HoldemGame(
                gameSettings,
                round
        );
        holdemGame.addPlayer(PlayerUtil.getDefaultPlayerForHoldem("1"));
        holdemGame.addPlayer(PlayerUtil.getDefaultPlayerForHoldem("2"));
        gameManager.createGame(randomGameName, holdemGame);
        executorServiceForGames.submit(holdemGame::start);
    }

    private void createGame(GameSettings gameSettings) {
        final String randomGameName = getRandomGOTCityName();
        final List<PlayerDTO> players = new ArrayList<>();
        final Round round = new HoldemRound(
                players,
                randomGameName,
                orderService,
                gameSettings.getStartSmallBlindBet(),
                gameSettings.getStartBigBlindBet());
        final Game holdemGame = new HoldemGame(
                gameSettings,
                round
        );
//        holdemGame.addPlayer(PlayerUtil.getDefaultPlayerForHoldem("3"));
        gameManager.createGame(randomGameName, holdemGame);
        executorServiceForGames.submit(holdemGame::start);
    }

}
