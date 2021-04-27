package ru.smn.poker.action;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.smn.poker.action.holdem.Raise;
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
public class RaiseTest {

    @SpyBean
    private ActionService actionService;

    @SpyBean
    private GameService gameService;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Test
    public void testSuccessRaise() throws InterruptedException {
        final long raiseCount = 4;
        final long lastBet = 2;
        final TableSettings tableSettings = getRoundSettingsDTO(lastBet);
        final PlayerEntity player = getPlayer();
        final Raise raise = new Raise(raiseCount);
        executorService.submit(() -> raise.doAction(tableSettings, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT - raise.getCount(), player.getTableSettings().getChipsCount().getCount());
    }

    @Test
    public void testFailRaise() throws InterruptedException {
        final long raiseCount = 2;
        final long lastBet = 2;
        final TableSettings tableSettings = getRoundSettingsDTO(lastBet);
        final PlayerEntity player = getPlayer();
        final Raise raise = new Raise(raiseCount);
        executorService.submit(() -> raise.doAction(tableSettings, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
        Mockito.verify(actionService, Mockito.times(1))
                .waitPlayerAction(player, tableSettings);
    }

}
