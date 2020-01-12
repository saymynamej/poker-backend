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
import ru.sm.poker.action.strategy.call.CallBigBlindStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.util.DTOUtilTest;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class CallBigBlindStrategyTest {

    private ActionStrategy actionStrategy = new CallBigBlindStrategy();

    @SpyBean
    private ActionService actionService;

    private final static long LAST_BET = 400L;

    @Test
    public void testExceptions() {
        final Player player = DTOUtilTest.getPlayer(RoleType.PLAYER);
        final RoundSettingsDTO roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO(LAST_BET, StageType.PREFLOP);

        final RuntimeException exceptionWithPlayer = Assertions.assertThrows(
                RuntimeException.class,
                () -> actionStrategy.execute(player, actionService, new Call(LAST_BET, player.getGameName()), roundSettingsDTO)
        );
        Assertions.assertEquals(RuntimeException.class, exceptionWithPlayer.getClass());

        player.setRole(RoleType.BUTTON);
        final RuntimeException exceptionWithButton = Assertions.assertThrows(
                RuntimeException.class,
                () -> actionStrategy.execute(player, actionService, new Call(LAST_BET, player.getGameName()), roundSettingsDTO)
        );
        Assertions.assertEquals(RuntimeException.class, exceptionWithButton.getClass());

        player.setRole(RoleType.SMALL_BLIND);
        final RuntimeException exceptionSmallBlind = Assertions.assertThrows(
                RuntimeException.class,
                () -> actionStrategy.execute(player, actionService, new Call(LAST_BET, player.getGameName()), roundSettingsDTO)
        );
        Assertions.assertEquals(RuntimeException.class, exceptionSmallBlind.getClass());
    }

    @Test
    public void testWhenWasRaiseAndCallIsIncorrectOnPreflop() {
        final Player player = DTOUtilTest.getPlayer(RoleType.BIG_BLIND);
        final RoundSettingsDTO roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO(LAST_BET, StageType.PREFLOP);
        actionStrategy.execute(player, actionService, new Call(LAST_BET, player.getGameName()), roundSettingsDTO);
        Mockito.verify(actionService).waitOneMoreAction(player, roundSettingsDTO);
    }

    @Test
    public void testWhenWasRaiseAndCallIsCorrectOnPreflop() {
        final Player player = DTOUtilTest.getPlayer(RoleType.BIG_BLIND);
        final RoundSettingsDTO roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO(LAST_BET, StageType.PREFLOP);
        player.removeChips(roundSettingsDTO.getBigBlindBet());
        final long bet = LAST_BET - roundSettingsDTO.getBigBlindBet();
        actionStrategy.execute(player, actionService, new Call(bet, player.getGameName()), roundSettingsDTO);
        Mockito.verify(actionService, Mockito.times(0)).waitOneMoreAction(Mockito.any(), Mockito.any());
        Assertions.assertEquals(DTOUtilTest.DEFAULT_CHIPS_COUNT - LAST_BET, player.getChipsCount());
    }

    @Test
    public void testWhenWasRaiseAndCallIsCorrectOnFlop() {
        final Player player = DTOUtilTest.getPlayer(RoleType.BIG_BLIND);
        final RoundSettingsDTO roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO(LAST_BET, StageType.FLOP);
        actionStrategy.execute(player, actionService, new Call(LAST_BET, player.getGameName()), roundSettingsDTO);
        Mockito.verify(actionService, Mockito.times(0)).waitOneMoreAction(Mockito.any(), Mockito.any());
        Assertions.assertEquals(DTOUtilTest.DEFAULT_CHIPS_COUNT - LAST_BET, player.getChipsCount());
    }
}
