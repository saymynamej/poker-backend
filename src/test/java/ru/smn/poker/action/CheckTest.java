package ru.smn.poker.action;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.smn.poker.action.holdem.Check;
import ru.smn.poker.dto.Player;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.enums.RoleType;
import ru.smn.poker.enums.StageType;
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
        final RoundSettings roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO(StageType.FLOP);
        roundSettingsDTO.setLastBet(0L);
        final Player player = DTOUtilTest.getPlayer();
        executorService.submit(() -> new Check().doAction(roundSettingsDTO, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
    }

    @Test
    public void testFailCheckWhenLastBetZero() throws InterruptedException {
        final RoundSettings roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO(StageType.PREFLOP);
        roundSettingsDTO.setLastBet(0L);
        final Player player = DTOUtilTest.getPlayer();
        executorService.submit(() -> new Check().doAction(roundSettingsDTO, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
        Mockito.verify(actionService, Mockito.times(1)).waitUntilPlayerWillHasAction(player, roundSettingsDTO);
    }

    @Test
    public void testSuccessCheckWhenPlayerOnBigBlind() throws InterruptedException {
        final RoundSettings roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO(StageType.PREFLOP);
        roundSettingsDTO.setLastBet(2L);
        final Player player = DTOUtilTest.getPlayer();
        player.setRole(RoleType.BIG_BLIND);
        executorService.submit(() -> new Check().doAction(roundSettingsDTO, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
    }

    @Test
    public void testFailCheckWhenPlayerOnBigBlind() throws InterruptedException {
        final RoundSettings roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO(StageType.PREFLOP);
        roundSettingsDTO.setLastBet(roundSettingsDTO.getBigBlindBet() + roundSettingsDTO.getBigBlindBet());
        final Player player = DTOUtilTest.getPlayer();
        executorService.submit(() -> new Check().doAction(roundSettingsDTO, player, gameService, actionService));
        executorService.awaitTermination(2L, TimeUnit.SECONDS);
        Mockito.verify(actionService, Mockito.times(1)).waitUntilPlayerWillHasAction(player, roundSettingsDTO);
    }
}
