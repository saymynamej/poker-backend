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
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;

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
        final TableSettings tableSettingsDTO = getRoundSettingsDTO(DEFAULT_LAST_BET);
        final Bet bet = new Bet(DEFAULT_LAST_BET);
        final PlayerEntity player = getPlayer();
        executorServiceForActions.submit(() -> bet.doAction(tableSettingsDTO, player, gameService, actionService));
        executorServiceForActions.awaitTermination(2L, TimeUnit.SECONDS);
        Mockito.verify(actionService, Mockito.times(1)).waitPlayerAction(player, tableSettingsDTO);
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT, player.getChipsCount().getCount());
    }

    @Test
    public void testSuccessBet() throws InterruptedException {
        final TableSettings tableSettingsDTO = getRoundSettingsDTO(0);
        final Bet bet = new Bet(DEFAULT_LAST_BET);
        final PlayerEntity player = getPlayer();
        executorServiceForActions.submit(() -> bet.doAction(tableSettingsDTO, player, gameService, actionService));
        executorServiceForActions.awaitTermination(2L, TimeUnit.SECONDS);
        Mockito.verify(actionService, Mockito.times(0)).waitPlayerAction(player, tableSettingsDTO);
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT - bet.getCount(), player.getChipsCount().getCount());
    }
}
