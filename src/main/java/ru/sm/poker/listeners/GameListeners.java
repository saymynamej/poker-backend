package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.config.game.holdem.HoldemFullTableSettings;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.game.Round;
import ru.sm.poker.game.holdem.HoldemGame;
import ru.sm.poker.game.holdem.HoldemRound;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.WinnerService;
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
    private final WinnerService winnerService;
    private final Queue<Player> players;
    private boolean isEnable = true;

    @PostConstruct
    public void init() {
        enableHoldemClassicCash();
        fillHoldemCashGames();
    }

    private void enableHoldemClassicCash() {
        executorServiceForStart.submit(() -> {
            while (isEnable) {
                ThreadUtil.sleep(1);
                if (players.size() >= 4) {
                    final String randomGameName = getRandomGOTCityName();

                    final List<Player> playersFromQueue = extractQueue();

                    final GameSettings gameSettings = new HoldemFullTableSettings(randomGameName, GameType.HOLDEM);

                    final Round round = new HoldemRound(
                            playersFromQueue,
                            randomGameName,
                            orderService,
                            winnerService,
                            gameSettings.getStartSmallBlindBet(),
                            gameSettings.getStartBigBlindBet());

                    final Game holdemGame = new HoldemGame(
                            gameSettings,
                            playersFromQueue,
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
        final int size = this.players.size();
        for (int i = 0; i < size; i++) {
            players.add(this.players.poll());
        }
        return players;
    }


    public void fillHoldemCashGames() {
        for (int i = 0; i < 10; i++) {
            final String randomGameName = getRandomGOTCityName();

            final GameSettings gameSettings = new HoldemFullTableSettings(randomGameName, GameType.HOLDEM);
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
                    players,
                    round
            );
            gameManager.createNewGame(randomGameName, holdemGame);
            executorServiceForGames.submit(holdemGame::start);
        }
    }

}
