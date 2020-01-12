package ru.sm.poker.action;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.action.holdem.Check;
import ru.sm.poker.action.strategy.ActionStrategy;
import ru.sm.poker.action.strategy.check.CheckCommonStrategy;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.util.DTOUtilTest;

import static ru.sm.poker.util.DTOUtilTest.getPlayer;
import static ru.sm.poker.util.DTOUtilTest.getRoundSettingsDTO;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class CheckCommonStrategyTest {

    private ActionStrategy actionStrategy = new CheckCommonStrategy();

    @SpyBean
    private ActionService actionService;

    private final static long LAST_BET = 2L;


    @Test
    void testCheckStrategyForBigBlindOnPreflop() {
        final RoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO(LAST_BET, StageType.PREFLOP);
        final Player player = getPlayer(RoleType.BIG_BLIND);
        actionStrategy.execute(player, actionService, new Check(), roundSettingsDTO);
        Mockito.verify(actionService, Mockito.times(0)).waitOneMoreAction(player, roundSettingsDTO);

        roundSettingsDTO.setLastBet(4L);
        actionStrategy.execute(player, actionService, new Check(), roundSettingsDTO);
        Mockito.verify(actionService, Mockito.times(1)).waitOneMoreAction(player, roundSettingsDTO);


        roundSettingsDTO.setLastBet(4L);
        player.setRole(RoleType.PLAYER);
        actionStrategy.execute(player, actionService, new Check(), roundSettingsDTO);
        Mockito.verify(actionService, Mockito.times(2)).waitOneMoreAction(player, roundSettingsDTO);


        Assertions.assertEquals(DTOUtilTest.DEFAULT_CHIPS_COUNT, player.getChipsCount());

    }

    @Test
    void testCheckStrategyForBigBlindOnFlop(){
        final RoundSettingsDTO roundSettingsDTOSuccess = getRoundSettingsDTO(0L, StageType.FLOP);
        final Player player = getPlayer(RoleType.PLAYER);
        actionStrategy.execute(player, actionService, new Check(), roundSettingsDTOSuccess);
        Mockito.verify(actionService, Mockito.times(0)).waitOneMoreAction(player, roundSettingsDTOSuccess);

        final RoundSettingsDTO roundSettingsDTOFail = getRoundSettingsDTO(2L, StageType.FLOP);
        actionStrategy.execute(player, actionService, new Check(), roundSettingsDTOFail);
        Mockito.verify(actionService, Mockito.times(1)).waitOneMoreAction(player, roundSettingsDTOFail);

        Assertions.assertEquals(DTOUtilTest.DEFAULT_CHIPS_COUNT, player.getChipsCount());

    }
}
