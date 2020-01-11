package ru.sm.poker.action;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.action.strategy.call.CallCommonStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

import java.util.Collections;
import java.util.Random;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class CallCommonStrategyTest {

    @SpyBean
    private ActionService actionService;

    private ActionStrategy actionStrategy = new CallCommonStrategy();

    private String gameName = "test";

    private final static long FULL_CHIPS_COUNT = 5000L;
    private final static long BET = 100L;
    private final static long EXPECTED_CHIPS_COUNT_AFTER_BET = FULL_CHIPS_COUNT - BET;

    @Test
    public void testSuccessStrategy() {
        final RoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO();
        final Player player = getPlayer();
        actionStrategy.execute(player, actionService, new Call(BET, gameName), roundSettingsDTO);
        Assertions.assertEquals(player.getChipsCount(), EXPECTED_CHIPS_COUNT_AFTER_BET);
        Assertions.assertEquals(roundSettingsDTO.getBank(), BET);
    }

    @Test
    public void testFailStrategy() {
        final RoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO();
        roundSettingsDTO.setLastBet(roundSettingsDTO.getLastBet() + new Random().nextInt(10));
        final Player player = getPlayer();
        actionStrategy.execute(player, actionService, new Call(BET, gameName), roundSettingsDTO);
        Mockito.verify(actionService).waitPlayerAction(player, roundSettingsDTO);
    }

    public RoundSettingsDTO getRoundSettingsDTO() {
        return RoundSettingsDTO.builder()
                .lastBet(BET)
                .players(Collections.emptyList())
                .build();
    }


    public Player getPlayer() {
        return Player.builder()
                .gameName(gameName)
                .chipsCount(FULL_CHIPS_COUNT)
                .roleType(RoleType.PLAYER)
                .build();
    }
}
