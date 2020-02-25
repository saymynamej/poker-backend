package ru.sm.poker.action;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.action.holdem.Raise;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.sm.poker.util.DTOUtilTest.*;
import static ru.sm.poker.util.WaitUtilTest.waitAction;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class RaiseTest {

    @SpyBean
    private ActionService actionService;

    @SpyBean
    private GameService gameService;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Test
    public void testSuccessRaise() {
        final long raiseCount = 4;
        final long lastBet = 2;
        final HoldemRoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO(lastBet);
        final PlayerDTO player = getPlayer();
        final Raise raise = new Raise(raiseCount);
        executorService.submit(() -> raise.doAction(roundSettingsDTO, player, gameService, actionService));
        waitAction(player, ActionType.RAISE);
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT - raise.getCount(), player.getChipsCount());
    }

    private long setHistoryForPlayer(PlayerDTO player, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final long firstBet = 4L;
        final long secondBet = 16L;
        final long lastBet = 64L;

        final Map<PlayerDTO, List<Action>> history = holdemRoundSettingsDTO.getStageHistory();
        history.put(player, List.of(
                new Raise(firstBet),
                new Raise(secondBet))
        );

        player.removeChips(secondBet);
        holdemRoundSettingsDTO.setLastBet(lastBet);

        return firstBet + secondBet;
    }
}
