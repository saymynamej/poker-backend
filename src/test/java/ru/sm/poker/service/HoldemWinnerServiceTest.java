package ru.sm.poker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.action.holdem.Raise;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.service.holdem.HoldemWinnerService;
import ru.sm.poker.util.DTOUtilTest;
import ru.sm.poker.util.HistoryUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class HoldemWinnerServiceTest {


    private static final long firstBet = 200;
    private static final long secondBet= 400;
    private static final long thirdBet = 1000;

    @Autowired
    private HoldemWinnerService holdemWinnerService;

    @Test
    public void testWhenPlayersHaveDifferentCombinations() {
        final PlayerDTO winner = DTOUtilTest.getPlayer();
        final PlayerDTO looser = DTOUtilTest.getPlayer();

        winner.addCards(Arrays.asList(CardType.K_C, CardType.A_C));
        looser.addCards(Arrays.asList(CardType.TWO_D, CardType.FIVE_H));

        final Map<PlayerDTO, List<CountAction>> fullHistory = Map.of(
                winner, Arrays.asList(new Call(firstBet), new Call(secondBet), new Call(thirdBet)),
                looser, Arrays.asList(new Raise(firstBet), new Raise(secondBet), new Raise(thirdBet))
        );

        final HoldemRoundSettingsDTO roundSettingsDTO = HoldemRoundSettingsDTO.builder()
                .flop(Arrays.asList(CardType.TWO_C, CardType.THREE_C, CardType.FOUR_C))
                .tern(CardType.SEVEN_H)
                .river(CardType.TEN_H)
                .bank(firstBet + secondBet + thirdBet * fullHistory.size())
                .fullHistory(fullHistory)
                .players(Arrays.asList(winner, looser))
                .build();

        final long firstBets = HistoryUtil.sumRoundHistoryBets(roundSettingsDTO, winner);
        final long secondBets = HistoryUtil.sumRoundHistoryBets(roundSettingsDTO, looser);
        winner.removeChips(firstBets);
        looser.removeChips(secondBets);

        holdemWinnerService.sendPrizes(roundSettingsDTO);

        Assertions.assertEquals(winner.getChipsCount(), DTOUtilTest.DEFAULT_CHIPS_COUNT + firstBets);
        Assertions.assertEquals(looser.getChipsCount(), DTOUtilTest.DEFAULT_CHIPS_COUNT - secondBets);
    }

    @Test
    public void testWhenPlayersHaveSameCombinations() {
        final PlayerDTO winner = DTOUtilTest.getPlayer();
        final PlayerDTO looser = DTOUtilTest.getPlayer();

        winner.addCards(Arrays.asList(CardType.TWO_H, CardType.THREE_S));
        looser.addCards(Arrays.asList(CardType.TWO_D, CardType.THREE_D));

        final Map<PlayerDTO, List<CountAction>> fullHistory = Map.of(
                winner, Arrays.asList(new Call(firstBet), new Call(secondBet), new Call(thirdBet)),
                looser, Arrays.asList(new Raise(firstBet), new Raise(secondBet), new Raise(thirdBet))
        );
        final HoldemRoundSettingsDTO roundSettingsDTO = HoldemRoundSettingsDTO.builder()
                .flop(Arrays.asList(CardType.TWO_C, CardType.THREE_C, CardType.FOUR_C))
                .tern(CardType.SEVEN_H)
                .river(CardType.TEN_H)
                .bank(firstBet + secondBet + thirdBet * fullHistory.size())
                .fullHistory(fullHistory)
                .players(Arrays.asList(winner, looser))
                .build();

        final long firstBets = HistoryUtil.sumRoundHistoryBets(roundSettingsDTO, winner);
        final long secondBets = HistoryUtil.sumRoundHistoryBets(roundSettingsDTO, looser);
        winner.removeChips(firstBets);
        looser.removeChips(secondBets);

        holdemWinnerService.sendPrizes(roundSettingsDTO);

        Assertions.assertEquals(winner.getChipsCount(), DTOUtilTest.DEFAULT_CHIPS_COUNT);
        Assertions.assertEquals(looser.getChipsCount(), DTOUtilTest.DEFAULT_CHIPS_COUNT);
    }


    @Test
    public void testWhenPlayersHaveSameCombinationsButDifferentPower() {
        final PlayerDTO winner = DTOUtilTest.getPlayer();
        final PlayerDTO looser = DTOUtilTest.getPlayer();

        winner.addCards(Arrays.asList(CardType.TWO_H, CardType.TEN_D));
        looser.addCards(Arrays.asList(CardType.TWO_D, CardType.THREE_D));

        final Map<PlayerDTO, List<CountAction>> fullHistory = Map.of(
                winner, Arrays.asList(new Call(firstBet), new Call(secondBet), new Call(thirdBet)),
                looser, Arrays.asList(new Raise(firstBet), new Raise(secondBet), new Raise(thirdBet))
        );
        final HoldemRoundSettingsDTO roundSettingsDTO = HoldemRoundSettingsDTO.builder()
                .flop(Arrays.asList(CardType.TWO_C, CardType.THREE_C, CardType.FOUR_C))
                .tern(CardType.SEVEN_H)
                .river(CardType.TEN_H)
                .bank(firstBet + secondBet + thirdBet * fullHistory.size())
                .fullHistory(fullHistory)
                .players(Arrays.asList(winner, looser))
                .build();

        final long firstBets = HistoryUtil.sumRoundHistoryBets(roundSettingsDTO, winner);
        final long secondBets = HistoryUtil.sumRoundHistoryBets(roundSettingsDTO, looser);
        winner.removeChips(firstBets);
        looser.removeChips(secondBets);

        holdemWinnerService.sendPrizes(roundSettingsDTO);

        Assertions.assertEquals(winner.getChipsCount(), DTOUtilTest.DEFAULT_CHIPS_COUNT + firstBets);
        Assertions.assertEquals(looser.getChipsCount(), DTOUtilTest.DEFAULT_CHIPS_COUNT - secondBets);
    }
}
