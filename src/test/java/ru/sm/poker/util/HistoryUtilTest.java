package ru.sm.poker.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.holdem.Bet;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.action.holdem.Raise;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.Player;
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
        final Map<Player, List<Action>> history = roundSettingsDTO.getStageHistory();
        final Player player = getPlayer();
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
        final Map<Player, List<Action>> history = roundSettingsDTO.getStageHistory();
        final Player player = getPlayer();
        history.put(player, List.of(new Call(firstBet), new Raise(secondRaise), new Raise(thirdRaise)));
        final long result = sumAllHistoryBetsWithNewAction(roundSettingsDTO, player, new Bet(newActionBet));
        Assertions.assertEquals(firstBet + secondRaise + thirdRaise + newActionBet, result);
    }

    @Test
    public void addActionInHistoryTest() {
        long callCount = 100;
        final HoldemRoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO();
        final Player player = getPlayer();
        player.setAction(new Call(callCount));
        addActionInHistory(roundSettingsDTO, player);
        final CountAction action = (CountAction) roundSettingsDTO.getStageHistory().get(player).get(0);
        Assertions.assertEquals(action.getCount(), callCount);
    }

    @Test
    public void addActionInHistoryTest2() {
        long callCount = 100;
        final HoldemRoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO();
        final Player player = getPlayer();
        addActionInHistory(roundSettingsDTO, player, new Call(callCount));
        CountAction action = (CountAction) roundSettingsDTO.getStageHistory().get(player).get(0);
        Assertions.assertEquals(action.getCount(), callCount);
    }

    @Test
    public void allPlayersInGameHaveSameCountOfBetSuccessTest() {
        final HoldemRoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO(DEFAULT_PLAYERS_SIZE, StageType.PREFLOP);
        final List<Player> players = roundSettingsDTO.getPlayers();
        for (Player player : players) {
            addActionInHistory(roundSettingsDTO, player, new Call(DEFAULT_BIG_BLIND_BET));
        }
        final boolean inGameHaveSameCountOfBet = allPlayersInGameHaveSameCountOfBet(roundSettingsDTO);
        Assertions.assertTrue(inGameHaveSameCountOfBet);
    }

    @Test
    public void allPlayersInGameHaveSameCountOfBetSuccessFail() {
        final HoldemRoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO(DEFAULT_PLAYERS_SIZE, StageType.PREFLOP);
        final List<Player> players = roundSettingsDTO.getPlayers();
        for (Player player : players) {
            addActionInHistory(roundSettingsDTO, player, new Call(DEFAULT_BIG_BLIND_BET));
        }
        addActionInHistory(roundSettingsDTO, players.get(0), new Call(DEFAULT_BIG_BLIND_BET));
        final boolean inGameHaveSameCountOfBet = allPlayersInGameHaveSameCountOfBet(roundSettingsDTO);
        Assertions.assertFalse(inGameHaveSameCountOfBet);
    }
}
