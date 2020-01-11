package ru.sm.poker.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.action.strategy.call.CallCommonStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

import java.util.Collections;
import java.util.Random;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class CallCommonStrategyTest {

    @SpyBean
    private ActionService actionService;

    private ActionStrategy actionStrategy = new CallCommonStrategy();

    private String gameName = "test";

    private final static long FULL_CHIPS_COUNT = 5000L;
    private final static long BET = 100L;
    private final static long EXPECTED_CHIPS_COUNT_AFTER_BET = FULL_CHIPS_COUNT - BET;

    @Test
    void testSuccessStrategy() {
        final RoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO();
        final Player player = getPlayer();
        actionStrategy.execute(player, actionService, new Call(BET, gameName), roundSettingsDTO);
        Assertions.assertEquals(player.getChipsCount(), EXPECTED_CHIPS_COUNT_AFTER_BET);
        Assertions.assertEquals(roundSettingsDTO.getBank(), BET);
    }

    @Test
    void testFailStrategy() {
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
