//package ru.smn.poker.util;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import ru.smn.poker.action.Action;
//import ru.smn.poker.action.CountAction;
//import ru.smn.poker.action.holdem.Bet;
//import ru.smn.poker.action.holdem.Call;
//import ru.smn.poker.action.holdem.Raise;
//import ru.smn.poker.entities.PlayerEntity;
//import ru.smn.poker.enums.StageType;
//import ru.smn.poker.game.TableSettings;
//
//import java.util.List;
//import java.util.Map;
//
//import static ru.smn.poker.util.DTOUtilTest.getRoundSettingsDTO;
//import static ru.smn.poker.util.HistoryUtil.*;
//
//public class HistoryUtilTest {
//
//    @Test
//    public void sumAllHistoryBets() {
//        final long firstBet = 2;
//        final long secondRaise = 10;
//        final long thirdRaise = 40;
//        final TableSettings tableSettingsDTO = DTOUtilTest.getRoundSettingsDTO();
//        final Map<PlayerEntity, List<Action>> history = tableSettingsDTO.getStageHistory();
//        final PlayerEntity player = DTOUtilTest.getPlayer();
//        history.put(player, List.of(new Call(firstBet), new Raise(secondRaise), new Raise(thirdRaise)));
//        final long result = HistoryUtil.sumStageHistoryBets(tableSettingsDTO, player);
//        Assertions.assertEquals(firstBet + secondRaise + thirdRaise, result);
//    }
//
//    @Test
//    public void sumAllHistoryBetsWithNewActionTest() {
//        final long firstBet = 2;
//        final long secondRaise = 10;
//        final long thirdRaise = 40;
//        final long newActionBet = 100;
//        final TableSettings tableSettingsDTO = DTOUtilTest.getRoundSettingsDTO();
//        final Map<PlayerEntity, List<Action>> history = tableSettingsDTO.getStageHistory();
//        final PlayerEntity player = DTOUtilTest.getPlayer();
//        history.put(player, List.of(new Call(firstBet), new Raise(secondRaise), new Raise(thirdRaise)));
//        final long result = sumAllHistoryBetsWithNewAction(tableSettingsDTO, player, new Bet(newActionBet));
//        Assertions.assertEquals(firstBet + secondRaise + thirdRaise + newActionBet, result);
//    }
//
//    @Test
//    public void addActionInHistoryTest() {
//        long callCount = 100;
//        final TableSettings tableSettingsDTO = DTOUtilTest.getRoundSettingsDTO();
//        final PlayerEntity player = DTOUtilTest.getPlayer();
//        player.getTableSettings().setAction(new Call(callCount));
//        addActionInStageHistory(tableSettingsDTO, player);
//        final CountAction action = (CountAction) tableSettingsDTO.getStageHistory().get(player).get(0);
//        Assertions.assertEquals(action.getCount(), callCount);
//    }
//
//    @Test
//    public void addActionInHistoryTest2() {
//        long callCount = 100;
//        final TableSettings tableSettingsDTO = DTOUtilTest.getRoundSettingsDTO();
//        final PlayerEntity player = DTOUtilTest.getPlayer();
//        addActionInStageHistory(tableSettingsDTO, player, new Call(callCount));
//        CountAction action = (CountAction) tableSettingsDTO.getStageHistory().get(player).get(0);
//        Assertions.assertEquals(action.getCount(), callCount);
//    }
//
//    @Test
//    public void allPlayersInGameHaveSameCountOfBetSuccessTest() {
//        final TableSettings tableSettingsDTO = getRoundSettingsDTO(DTOUtilTest.DEFAULT_PLAYERS_SIZE, StageType.PREFLOP);
//        final List<PlayerEntity> players = tableSettingsDTO.getPlayers();
//        for (PlayerEntity player : players) {
//            final Call action = new Call(DTOUtilTest.DEFAULT_BIG_BLIND_BET);
//            player.getTableSettings().setAction(action);
//            addActionInStageHistory(tableSettingsDTO, player, action);
//        }
//        final boolean inGameHaveSameCountOfBet = allPlayersInGameHaveSameCountOfBet(tableSettingsDTO);
//        Assertions.assertTrue(inGameHaveSameCountOfBet);
//    }
//
//    @Test
//    public void allPlayersInGameHaveSameCountOfBetSuccessFail() {
//        final TableSettings tableSettingsDTO = getRoundSettingsDTO(DTOUtilTest.DEFAULT_PLAYERS_SIZE, StageType.PREFLOP);
//        final List<PlayerEntity> players = tableSettingsDTO.getPlayers();
//        for (PlayerEntity player : players) {
//            final Call action = new Call(DTOUtilTest.DEFAULT_BIG_BLIND_BET);
//            player.getTableSettings().setAction(action);
//            addActionInStageHistory(tableSettingsDTO, player, action);
//        }
//        addActionInStageHistory(tableSettingsDTO, players.get(0), new Call(DTOUtilTest.DEFAULT_BIG_BLIND_BET));
//        final boolean inGameHaveSameCountOfBet = allPlayersInGameHaveSameCountOfBet(tableSettingsDTO);
//        Assertions.assertFalse(inGameHaveSameCountOfBet);
//    }
//}
