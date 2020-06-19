package ru.sm.poker.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.action.holdem.Bet;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.sm.poker.util.DTOUtilTest.*;
import static ru.sm.poker.util.WaitUtilTest.waitAction;

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
    public void testFailBet(){
        final HoldemRoundSettings roundSettingsDTO = getRoundSettingsDTO(DEFAULT_LAST_BET);
        final Bet bet = new Bet(DEFAULT_LAST_BET);
        final Player player = getPlayer();
        executorServiceForActions.submit(() -> bet.doAction(roundSettingsDTO, player, gameService, actionService));
        waitAction(player);
        Mockito.verify(actionService, Mockito.times(1)).waitUntilPlayerWillHasAction(player, roundSettingsDTO);
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT, player.getChipsCount());
    }

    @Test
    public void testSuccessBet(){
        final HoldemRoundSettings roundSettingsDTO = getRoundSettingsDTO(0);
        final Bet bet = new Bet(DEFAULT_LAST_BET);
        final Player player = getPlayer();
        executorServiceForActions.submit(() -> bet.doAction(roundSettingsDTO, player, gameService, actionService));
        waitAction(player);
        Mockito.verify(actionService, Mockito.times(0)).waitUntilPlayerWillHasAction(player, roundSettingsDTO);
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT - bet.getCount(), player.getChipsCount());
    }
}
