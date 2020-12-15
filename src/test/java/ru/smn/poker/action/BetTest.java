package ru.smn.poker.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.smn.poker.action.holdem.Bet;
import ru.smn.poker.dto.Player;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static ru.smn.poker.util.DTOUtilTest.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class BetTest {

    @Autowired
    private GameService gameService;

    @SpyBean
    private ActionService actionService;

    private ExecutorService executorServiceForActions = Executors.newSingleThreadExecutor();

    @Test
    public void testFailBet() throws InterruptedException {
        final RoundSettings roundSettingsDTO = getRoundSettingsDTO(DEFAULT_LAST_BET);
        final Bet bet = new Bet(DEFAULT_LAST_BET);
        final Player player = getPlayer();
        executorServiceForActions.submit(() -> bet.doAction(roundSettingsDTO, player, gameService, actionService));
        executorServiceForActions.awaitTermination(2L, TimeUnit.SECONDS);
        Mockito.verify(actionService, Mockito.times(1)).waitUntilPlayerWillHasAction(player, roundSettingsDTO);
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT, player.getChipsCount());
    }

    @Test
    public void testSuccessBet() throws InterruptedException {
        final RoundSettings roundSettingsDTO = getRoundSettingsDTO(0);
        final Bet bet = new Bet(DEFAULT_LAST_BET);
        final Player player = getPlayer();
        executorServiceForActions.submit(() -> bet.doAction(roundSettingsDTO, player, gameService, actionService));
        executorServiceForActions.awaitTermination(2L, TimeUnit.SECONDS);
        Mockito.verify(actionService, Mockito.times(0)).waitUntilPlayerWillHasAction(player, roundSettingsDTO);
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT - bet.getCount(), player.getChipsCount());
    }
}
