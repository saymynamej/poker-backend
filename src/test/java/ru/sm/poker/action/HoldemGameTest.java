package ru.sm.poker.action;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.game.common.CommonGameManager;
import ru.sm.poker.service.WinnerService;
import ru.sm.poker.service.common.OrderActionService;
import ru.sm.poker.service.common.SecurityNotificationService;
import ru.sm.poker.service.common.SimpleNotificationService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class HoldemGameTest {

    @Autowired
    private OrderActionService orderActionService;

    @Autowired
    private WinnerService winnerService;

    @Autowired
    private CommonGameManager commonGameManager;

    @MockBean
    private SimpleNotificationService simpleNotificationService;

    @MockBean
    private SecurityNotificationService securityNotificationService;


    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Test
    public void testGame() {





//        final String randomGameName = getRandomGOTCityName();
//
//        final List<Player> playersFromQueue = of(
//                getDefaultPlayerForHoldem("1"),
//                getDefaultPlayerForHoldem("2"),
//                getDefaultPlayerForHoldem("3"),
//                getDefaultPlayerForHoldem("4"),
//                getDefaultPlayerForHoldem("5")
//        );
//
//        final GameSettings gameSettings = new HoldemFullTableSettings(randomGameName);
//
//        final Round round = new HoldemRound(
//                playersFromQueue,
//                randomGameName,
//                orderActionService,
//                winnerService,
//                gameSettings.getStartSmallBlindBet(),
//                gameSettings.getStartBigBlindBet());
//
//        final Game holdemGame = new HoldemGame(
//                gameSettings,
//                playersFromQueue,
//                round
//        );
//
//        executorService.submit(() -> {
//            holdemGameManager.createNewGame(randomGameName, holdemGame);
//            holdemGameManager.startGame(randomGameName);
//        });
//
//        ThreadUtil.sleep(5);
//
//        final Player activePlayer = holdemGameManager.getActivePlayerInGame(randomGameName);
//        activePlayer.setAction(new Raise(40));
//        ThreadUtil.sleep(5);
    }
}
