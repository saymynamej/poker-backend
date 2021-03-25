package ru.smn.poker.action;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.smn.poker.action.holdem.Check;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.RoleType;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.common.GameService;
import ru.smn.poker.util.DTOUtilTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class CheckTest {

    @SpyBean
    private GameService gameService;
    @SpyBean
    private ActionService actionService;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Test
    public void testSuccessCheckWhenLastBetZero() throws InterruptedException {
        final TableSettings tableSettingsDTO = DTOUtilTest.getRoundSettingsDTO(StageType.FLOP);
        tableSettingsDTO.setLastBet(0L);
        final PlayerEntity player = DTOUtilTest.getPlayer();
        executorService.submit(() -> new Check().doAction(tableSettingsDTO, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
    }

    @Test
    public void testFailCheckWhenLastBetZero() throws InterruptedException {
        final TableSettings tableSettingsDTO = DTOUtilTest.getRoundSettingsDTO(StageType.PREFLOP);
        tableSettingsDTO.setLastBet(0L);
        final PlayerEntity player = DTOUtilTest.getPlayer();
        executorService.submit(() -> new Check().doAction(tableSettingsDTO, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
        Mockito.verify(actionService, Mockito.times(1)).waitPlayerAction(player, tableSettingsDTO);
    }

    @Test
    public void testSuccessCheckWhenPlayerOnBigBlind() throws InterruptedException {
        final TableSettings tableSettingsDTO = DTOUtilTest.getRoundSettingsDTO(StageType.PREFLOP);
        tableSettingsDTO.setLastBet(2L);
        final PlayerEntity player = DTOUtilTest.getPlayer();
        player.getTableSettings().setRole(RoleType.BIG_BLIND);
        executorService.submit(() -> new Check().doAction(tableSettingsDTO, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
    }

    @Test
    public void testFailCheckWhenPlayerOnBigBlind() throws InterruptedException {
        final TableSettings tableSettingsDTO = DTOUtilTest.getRoundSettingsDTO(StageType.PREFLOP);
        tableSettingsDTO.setLastBet(tableSettingsDTO.getBigBlindBet() + tableSettingsDTO.getBigBlindBet());
        final PlayerEntity player = DTOUtilTest.getPlayer();
        executorService.submit(() -> new Check().doAction(tableSettingsDTO, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
        Mockito.verify(actionService, Mockito.times(1)).waitPlayerAction(player, tableSettingsDTO);
    }
}
