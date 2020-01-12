package ru.sm.poker.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.action.strategy.call.CallSmallBlindStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.util.DTOUtilTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.sm.poker.util.DTOUtilTest.getPlayer;
import static ru.sm.poker.util.DTOUtilTest.getRoundSettingsDTO;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class CallSmallBlindStrategyTest {
    private ActionStrategy actionStrategy = new CallSmallBlindStrategy();

    @SpyBean
    private ActionService actionService;

    private final static long LAST_BET = 4L;
    private final static long FAIL_BET = 2L;
    private final static long SUCCESS_BET = 3L;

    @Test
    void testExceptions() {
        final RoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO(LAST_BET, StageType.PREFLOP);
        final Player player = getPlayer(RoleType.BIG_BLIND);

        final RuntimeException exceptionWithBigBlind = Assertions.assertThrows(
                RuntimeException.class,
                () -> actionStrategy.execute(player, actionService, new Call(SUCCESS_BET), roundSettingsDTO)
        );
        Assertions.assertEquals(RuntimeException.class, exceptionWithBigBlind.getClass());

        player.setRole(RoleType.BUTTON);

        final RuntimeException exceptionWithButton = Assertions.assertThrows(
                RuntimeException.class,
                () -> actionStrategy.execute(player, actionService, new Call(SUCCESS_BET), roundSettingsDTO)
        );
        Assertions.assertEquals(RuntimeException.class, exceptionWithButton.getClass());

        player.setRole(RoleType.PLAYER);

        final RuntimeException exceptionWithPlayer = Assertions.assertThrows(
                RuntimeException.class,
                () -> actionStrategy.execute(player, actionService, new Call(SUCCESS_BET), roundSettingsDTO)
        );
        Assertions.assertEquals(RuntimeException.class, exceptionWithPlayer.getClass());

    }

    @Test
    void testSuccessSmallBlindStrategyOnPreflop() {
        final RoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO(LAST_BET, StageType.PREFLOP);
        final Player player = getPlayer(RoleType.SMALL_BLIND);
        player.setChipsCount(player.getChipsCount() - roundSettingsDTO.getSmallBlindBet());
        actionStrategy.execute(player, actionService, new Call(SUCCESS_BET), roundSettingsDTO);
        Assertions.assertEquals(DTOUtilTest.DEFAULT_CHIPS_COUNT - LAST_BET, player.getChipsCount());
        verify(actionService, times(0)).waitOneMoreAction(player, roundSettingsDTO);
    }

    @Test
    void testFailSmallBlindStrategyOnPreflop() {
        final RoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO(LAST_BET, StageType.PREFLOP);
        final Player player = getPlayer(RoleType.SMALL_BLIND);
        actionStrategy.execute(player, actionService, new Call(FAIL_BET), roundSettingsDTO);
        Assertions.assertEquals(DTOUtilTest.DEFAULT_CHIPS_COUNT, player.getChipsCount());
        verify(actionService, times(1)).waitOneMoreAction(player, roundSettingsDTO);
    }

    @Test
    void testSuccessSmallBlindStrategyOnFlop() {
        final RoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO(LAST_BET, StageType.FLOP);
        final Player player = getPlayer(RoleType.SMALL_BLIND);
        actionStrategy.execute(player, actionService, new Call(LAST_BET), roundSettingsDTO);
        Assertions.assertEquals(DTOUtilTest.DEFAULT_CHIPS_COUNT - LAST_BET, player.getChipsCount());
        verify(actionService, times(0)).waitOneMoreAction(player, roundSettingsDTO);
    }

    @Test
    void testFailSmallBlindStrategyOnFlop() {
        final RoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO(LAST_BET, StageType.FLOP);
        final Player player = getPlayer(RoleType.SMALL_BLIND);
        actionStrategy.execute(player, actionService, new Call(FAIL_BET), roundSettingsDTO);
        Assertions.assertEquals(DTOUtilTest.DEFAULT_CHIPS_COUNT, player.getChipsCount());
        verify(actionService, times(1)).waitOneMoreAction(player, roundSettingsDTO);
    }

}
