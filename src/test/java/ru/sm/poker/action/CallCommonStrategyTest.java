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
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

import static ru.sm.poker.util.DTOUtilTest.getPlayer;
import static ru.sm.poker.util.DTOUtilTest.getRoundSettingsDTO;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class CallCommonStrategyTest {

    @SpyBean
    private ActionService actionService;

    private ActionStrategy actionStrategy = new CallCommonStrategy();

    private final static String GAME_NAME = "test";

    private final static long FULL_CHIPS_COUNT = 5000L;
    private final static long BET = 100L;
    private final static long EXPECTED_CHIPS_COUNT_AFTER_BET = FULL_CHIPS_COUNT - BET;

    @Test
    void testSuccessStrategy() {
        final RoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO(BET);
        final Player player = getPlayer(GAME_NAME, FULL_CHIPS_COUNT);
        actionStrategy.execute(player, actionService, new Call(BET, GAME_NAME), roundSettingsDTO);
        Assertions.assertEquals(player.getChipsCount(), EXPECTED_CHIPS_COUNT_AFTER_BET);
        Assertions.assertEquals(roundSettingsDTO.getBank(), BET);
    }

    @Test
    void testFailStrategy() {
        final RoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO(BET);
        roundSettingsDTO.setLastBet(roundSettingsDTO.getLastBet() + 1);
        final Player player = getPlayer(GAME_NAME, FULL_CHIPS_COUNT);
        actionStrategy.execute(player, actionService, new Call(BET, GAME_NAME), roundSettingsDTO);
        Mockito.verify(actionService).waitPlayerAction(player, roundSettingsDTO);
    }

}
