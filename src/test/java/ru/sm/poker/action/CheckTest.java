package ru.sm.poker.action;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.action.holdem.Check;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;
import ru.sm.poker.util.DTOUtilTest;

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
        final HoldemRoundSettingsDTO roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO(StageType.FLOP);
        roundSettingsDTO.setLastBet(0L);
        final PlayerDTO player = DTOUtilTest.getPlayer();
        executorService.submit(() -> new Check().doAction(roundSettingsDTO, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
    }

    @Test
    public void testFailCheckWhenLastBetZero() throws InterruptedException {
        final HoldemRoundSettingsDTO roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO(StageType.PREFLOP);
        roundSettingsDTO.setLastBet(0L);
        final PlayerDTO player = DTOUtilTest.getPlayer();
        executorService.submit(() -> new Check().doAction(roundSettingsDTO, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
        Mockito.verify(actionService, Mockito.times(1)).waitUntilPlayerWillHasAction(player, roundSettingsDTO);
    }

    @Test
    public void testSuccessCheckWhenPlayerOnBigBlind() throws InterruptedException {
        final HoldemRoundSettingsDTO roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO(StageType.PREFLOP);
        roundSettingsDTO.setLastBet(2L);
        final PlayerDTO player = DTOUtilTest.getPlayer();
        player.setRole(RoleType.BIG_BLIND);
        executorService.submit(() -> new Check().doAction(roundSettingsDTO, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
    }

    @Test
    public void testFailCheckWhenPlayerOnBigBlind() throws InterruptedException {
        final HoldemRoundSettingsDTO roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO(StageType.PREFLOP);
        roundSettingsDTO.setLastBet(roundSettingsDTO.getBigBlindBet() + roundSettingsDTO.getBigBlindBet());
        final PlayerDTO player = DTOUtilTest.getPlayer();
        executorService.submit(() -> new Check().doAction(roundSettingsDTO, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
        Mockito.verify(actionService, Mockito.times(1)).waitUntilPlayerWillHasAction(player, roundSettingsDTO);
    }
}
