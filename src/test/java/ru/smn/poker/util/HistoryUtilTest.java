package ru.smn.poker.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.action.holdem.Bet;
import ru.smn.poker.action.holdem.Call;
import ru.smn.poker.action.holdem.Raise;
import ru.smn.poker.dto.Player;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.RoundSettings;

import java.util.List;
import java.util.Map;

import static ru.smn.poker.util.DTOUtilTest.getRoundSettingsDTO;
import static ru.smn.poker.util.HistoryUtil.*;

public class HistoryUtilTest {

    @Test
    public void sumAllHistoryBets() {
        final long firstBet = 2;
        final long secondRaise = 10;
        final long thirdRaise = 40;
        final RoundSettings roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO();
        final Map<Player, List<Action>> history = roundSettingsDTO.getStageHistory();
        final Player player = DTOUtilTest.getPlayer();
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
        final RoundSettings roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO();
        final Map<Player, List<Action>> history = roundSettingsDTO.getStageHistory();
        final Player player = DTOUtilTest.getPlayer();
        history.put(player, List.of(new Call(firstBet), new Raise(secondRaise), new Raise(thirdRaise)));
        final long result = sumAllHistoryBetsWithNewAction(roundSettingsDTO, player, new Bet(newActionBet));
        Assertions.assertEquals(firstBet + secondRaise + thirdRaise + newActionBet, result);
    }

    @Test
    public void addActionInHistoryTest() {
        long callCount = 100;
        final RoundSettings roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO();
        final Player player = DTOUtilTest.getPlayer();
        player.setAction(new Call(callCount));
        addActionInHistory(roundSettingsDTO, player);
        final CountAction action = (CountAction) roundSettingsDTO.getStageHistory().get(player).get(0);
        Assertions.assertEquals(action.getCount(), callCount);
    }

    @Test
    public void addActionInHistoryTest2() {
        long callCount = 100;
        final RoundSettings roundSettingsDTO = DTOUtilTest.getRoundSettingsDTO();
        final Player player = DTOUtilTest.getPlayer();
        addActionInHistory(roundSettingsDTO, player, new Call(callCount));
        CountAction action = (CountAction) roundSettingsDTO.getStageHistory().get(player).get(0);
        Assertions.assertEquals(action.getCount(), callCount);
    }

    @Test
    public void allPlayersInGameHaveSameCountOfBetSuccessTest() {
        final RoundSettings roundSettingsDTO = getRoundSettingsDTO(DTOUtilTest.DEFAULT_PLAYERS_SIZE, StageType.PREFLOP);
        final List<Player> players = roundSettingsDTO.getPlayers();
        for (Player player : players) {
            addActionInHistory(roundSettingsDTO, player, new Call(DTOUtilTest.DEFAULT_BIG_BLIND_BET));
        }
        final boolean inGameHaveSameCountOfBet = allPlayersInGameHaveSameCountOfBet(roundSettingsDTO);
        Assertions.assertTrue(inGameHaveSameCountOfBet);
    }

    @Test
    public void allPlayersInGameHaveSameCountOfBetSuccessFail() {
        final RoundSettings roundSettingsDTO = getRoundSettingsDTO(DTOUtilTest.DEFAULT_PLAYERS_SIZE, StageType.PREFLOP);
        final List<Player> players = roundSettingsDTO.getPlayers();
        for (Player player : players) {
            addActionInHistory(roundSettingsDTO, player, new Call(DTOUtilTest.DEFAULT_BIG_BLIND_BET));
        }
        addActionInHistory(roundSettingsDTO, players.get(0), new Call(DTOUtilTest.DEFAULT_BIG_BLIND_BET));
        final boolean inGameHaveSameCountOfBet = allPlayersInGameHaveSameCountOfBet(roundSettingsDTO);
        Assertions.assertFalse(inGameHaveSameCountOfBet);
    }
}
