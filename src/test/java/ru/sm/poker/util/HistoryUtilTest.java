package ru.sm.poker.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.holdem.Bet;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.action.holdem.Raise;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.StageType;

import java.util.List;
import java.util.Map;

import static ru.sm.poker.util.DTOUtilTest.*;
import static ru.sm.poker.util.HistoryUtil.*;

public class HistoryUtilTest {

    @Test
    public void sumAllHistoryBets() {
        final long firstBet = 2;
        final long secondRaise = 10;
        final long thirdRaise = 40;
        final HoldemRoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO();
        final Map<PlayerDTO, List<CountAction>> history = roundSettingsDTO.getStageHistory();
        final PlayerDTO player = getPlayer();
        history.put(player, List.of(new Call(firstBet), new Raise(secondRaise), new Raise(thirdRaise)));
        final long result = HistoryUtil.sumStageHistoryBets(roundSettingsDTO, player);
        Assertions.assertEquals(firstBet + secondRaise + thirdRaise, result);
    }

    @Test
    public void sumAllHistoryBetsWithNewActionTest() {
        final long firstBet = 2;
        final long secondRaise = 10;
        final long thirdRaise = 40;
        final long newActionBet = 100;
        final HoldemRoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO();
        final Map<PlayerDTO, List<CountAction>> history = roundSettingsDTO.getStageHistory();
        final PlayerDTO player = getPlayer();
        history.put(player, List.of(new Call(firstBet), new Raise(secondRaise), new Raise(thirdRaise)));
        final long result = sumAllHistoryBetsWithNewAction(roundSettingsDTO, player, new Bet(newActionBet));
        Assertions.assertEquals(firstBet + secondRaise + thirdRaise + newActionBet, result);
    }

    @Test
    public void addActionInHistoryTest() {
        long callCount = 100;
        final HoldemRoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO();
        final PlayerDTO player = getPlayer();
        player.setAction(new Call(callCount));
        addActionInHistory(roundSettingsDTO, player);
        Assertions.assertEquals(roundSettingsDTO.getStageHistory().get(player).get(0).getCount(), callCount);
    }

    @Test
    public void addActionInHistoryTest2() {
        long callCount = 100;
        final HoldemRoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO();
        final PlayerDTO player = getPlayer();
        addActionInHistory(roundSettingsDTO, player, new Call(callCount));
        Assertions.assertEquals(roundSettingsDTO.getStageHistory().get(player).get(0).getCount(), callCount);
    }

    @Test
    public void allPlayersInGameHaveSameCountOfBetSuccessTest() {
        final HoldemRoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO(DEFAULT_PLAYERS_SIZE, StageType.PREFLOP);
        final List<PlayerDTO> playerDTOS = roundSettingsDTO.getPlayers();
        for (PlayerDTO playerDTO : playerDTOS) {
            addActionInHistory(roundSettingsDTO, playerDTO, new Call(DEFAULT_BIG_BLIND_BET));
        }
        final boolean inGameHaveSameCountOfBet = allPlayersInGameHaveSameCountOfBet(roundSettingsDTO);
        Assertions.assertTrue(inGameHaveSameCountOfBet);
    }

    @Test
    public void allPlayersInGameHaveSameCountOfBetSuccessFail() {
        final HoldemRoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO(DEFAULT_PLAYERS_SIZE, StageType.PREFLOP);
        final List<PlayerDTO> playerDTOS = roundSettingsDTO.getPlayers();
        for (PlayerDTO playerDTO : playerDTOS) {
            addActionInHistory(roundSettingsDTO, playerDTO, new Call(DEFAULT_BIG_BLIND_BET));
        }
        addActionInHistory(roundSettingsDTO, playerDTOS.get(0), new Call(DEFAULT_BIG_BLIND_BET));
        final boolean inGameHaveSameCountOfBet = allPlayersInGameHaveSameCountOfBet(roundSettingsDTO);
        Assertions.assertFalse(inGameHaveSameCountOfBet);
    }
}