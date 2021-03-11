package ru.smn.poker.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.smn.poker.action.holdem.Call;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.smn.poker.util.DTOUtilTest.*;


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
    void testSuccessCall() throws InterruptedException {
        final TableSettings tableSettingsDTO = getRoundSettingsDTO(DEFAULT_LAST_BET);
        final Call call = new Call(DEFAULT_LAST_BET);
        final PlayerEntity player = getPlayer();
        executorServiceForActions.submit(() -> call.doAction(tableSettingsDTO, player, gameService, actionService));
        executorServiceForActions.awaitTermination(2L, TimeUnit.SECONDS);
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT - DEFAULT_LAST_BET, player.getChipsCount().getCount());
        Assertions.assertEquals(call.getCount(), tableSettingsDTO.getLastBet());
    }

    @Test
    void testCallWhenChipsAreNotEnough() throws InterruptedException {
        final TableSettings tableSettingsDTO = getRoundSettingsDTO(DEFAULT_LAST_BET);
        final PlayerEntity player = getPlayer();
        final Call call = new Call(player.getChipsCount().getCount() + 1);
        executorServiceForActions.submit(() -> call.doAction(tableSettingsDTO, player, gameService, actionService));
        executorServiceForActions.awaitTermination(2L, TimeUnit.SECONDS);
        verify(actionService, times(1)).waitPlayerAction(player, tableSettingsDTO);
        Assertions.assertEquals(tableSettingsDTO.getBigBlindBet(), tableSettingsDTO.getLastBet());
    }

    @Test
    void testFailBet() throws InterruptedException {
        final TableSettings tableSettingsDTO = getRoundSettingsDTO(DEFAULT_LAST_BET);
        final PlayerEntity player = getPlayer();
        final Call call = new Call(DEFAULT_BIG_BLIND_BET + 1);
        executorServiceForActions.submit(() -> call.doAction(tableSettingsDTO, player, gameService, actionService));
        executorServiceForActions.awaitTermination(2L, TimeUnit.SECONDS);
        verify(actionService, times(1)).waitPlayerAction(player, tableSettingsDTO);
        Assertions.assertEquals(tableSettingsDTO.getBigBlindBet(), tableSettingsDTO.getLastBet());
    }

    @Test
    void testSuccessCallWithHistory() throws InterruptedException {
        final TableSettings tableSettingsDTO = getRoundSettingsDTO(DEFAULT_LAST_BET);
        final PlayerEntity player = getPlayer();
        final long sumAllBets = setHistoryForPlayer(player, tableSettingsDTO);
        final Call call = new Call(tableSettingsDTO.getLastBet() - sumAllBets);
        executorServiceForActions.submit(() -> call.doAction(tableSettingsDTO, player, gameService, actionService));
        executorServiceForActions.awaitTermination(2L, TimeUnit.SECONDS);
        Assertions.assertEquals(player.getChipsCount().getCount(), DEFAULT_CHIPS_COUNT - tableSettingsDTO.getLastBet());
    }


    @Test
    void testFailCallWithHistory() throws InterruptedException {
        final TableSettings tableSettingsDTO = getRoundSettingsDTO(DEFAULT_LAST_BET);
        final PlayerEntity player = getPlayer();
        final long sumAllBets = setHistoryForPlayer(player, tableSettingsDTO);
        final Call call = new Call(tableSettingsDTO.getLastBet() - sumAllBets + 1L);
        executorServiceForActions.submit(() -> call.doAction(tableSettingsDTO, player, gameService, actionService));
        executorServiceForActions.awaitTermination(2L, TimeUnit.SECONDS);
        verify(actionService, times(1)).waitPlayerAction(player, tableSettingsDTO);
        Assertions.assertEquals(player.getChipsCount().getCount(), DEFAULT_CHIPS_COUNT - sumAllBets);
    }

    private long setHistoryForPlayer(PlayerEntity player, TableSettings tableSettings) {
        final long firstBet = 2L;
        final long secondBet = 8L;
        final long thirdBet = 100L;

        final Map<PlayerEntity, List<Action>> history = tableSettings.getStageHistory();
        history.put(player, List.of(
                new Call(firstBet),
                new Call(secondBet),
                new Call(thirdBet))
        );

        player.removeChips(firstBet + secondBet + thirdBet);
        tableSettings.setLastBet(500L);

        return firstBet + secondBet + thirdBet;
    }

}
