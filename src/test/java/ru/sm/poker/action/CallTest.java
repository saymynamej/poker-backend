package ru.sm.poker.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.GameService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.sm.poker.util.DTOUtilTest.*;
import static ru.sm.poker.util.WaitUtilTest.waitAction;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class CallTest {

    @Autowired
    private GameService gameService;

    @SpyBean
    private ActionService actionService;

    private ExecutorService executorServiceForActions = Executors.newSingleThreadExecutor();

    @Test
    void testSuccessCall() {
        final HoldemRoundSettings roundSettingsDTO = getRoundSettingsDTO(DEFAULT_LAST_BET);
        final Call call = new Call(DEFAULT_LAST_BET);
        final Player player = getPlayer();
        executorServiceForActions.submit(() -> call.doAction(roundSettingsDTO, player, gameService, actionService));
        waitAction(player);
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT - DEFAULT_LAST_BET, player.getChipsCount());
        Assertions.assertEquals(call.getCount(), roundSettingsDTO.getLastBet());
    }

    @Test
    void testCallWhenChipsAreNotEnough() {
        final HoldemRoundSettings roundSettingsDTO = getRoundSettingsDTO(DEFAULT_LAST_BET);
        final Player player = getPlayer();
        final Call call = new Call(player.getChipsCount() + 1);
        executorServiceForActions.submit(() -> call.doAction(roundSettingsDTO, player, gameService, actionService));
        waitAction(player);
        verify(actionService, times(1)).waitUntilPlayerWillHasAction(player, roundSettingsDTO);
        Assertions.assertEquals(roundSettingsDTO.getBigBlindBet(), roundSettingsDTO.getLastBet());
    }

    @Test
    void testFailBet() {
        final HoldemRoundSettings roundSettingsDTO = getRoundSettingsDTO(DEFAULT_LAST_BET);
        final Player player = getPlayer();
        final Call call = new Call(DEFAULT_BIG_BLIND_BET + 1);
        executorServiceForActions.submit(() -> call.doAction(roundSettingsDTO, player, gameService, actionService));
        waitAction(player);
        verify(actionService, times(1)).waitUntilPlayerWillHasAction(player, roundSettingsDTO);
        Assertions.assertEquals(roundSettingsDTO.getBigBlindBet(), roundSettingsDTO.getLastBet());
    }

    @Test
    void testSuccessCallWithHistory() {
        final HoldemRoundSettings roundSettingsDTO = getRoundSettingsDTO(DEFAULT_LAST_BET);
        final Player player = getPlayer();
        final long sumAllBets = setHistoryForPlayer(player, roundSettingsDTO);
        final Call call = new Call(roundSettingsDTO.getLastBet() - sumAllBets);
        executorServiceForActions.submit(() -> call.doAction(roundSettingsDTO, player, gameService, actionService));
        waitAction(player);
        Assertions.assertEquals(player.getChipsCount(), DEFAULT_CHIPS_COUNT - roundSettingsDTO.getLastBet());
    }


    @Test
    void testFailCallWithHistory() {
        final HoldemRoundSettings roundSettingsDTO = getRoundSettingsDTO(DEFAULT_LAST_BET);
        final Player player = getPlayer();
        final long sumAllBets = setHistoryForPlayer(player, roundSettingsDTO);
        final Call call = new Call(roundSettingsDTO.getLastBet() - sumAllBets + 1L);
        executorServiceForActions.submit(() -> call.doAction(roundSettingsDTO, player, gameService, actionService));
        waitAction(player);
        verify(actionService, times(1)).waitUntilPlayerWillHasAction(player, roundSettingsDTO);
        Assertions.assertEquals(player.getChipsCount(), DEFAULT_CHIPS_COUNT - sumAllBets);
    }

    private long setHistoryForPlayer(Player player, HoldemRoundSettings holdemRoundSettings) {
        final long firstBet = 2L;
        final long secondBet = 8L;
        final long thirdBet = 100L;

        final Map<Player, List<Action>> history = holdemRoundSettings.getStageHistory();
        history.put(player, List.of(
                new Call(firstBet),
                new Call(secondBet),
                new Call(thirdBet))
        );

        player.removeChips(firstBet + secondBet + thirdBet);
        holdemRoundSettings.setLastBet(500L);

        return firstBet + secondBet + thirdBet;
    }

}
