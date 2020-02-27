package ru.sm.poker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.action.holdem.Fold;
import ru.sm.poker.action.holdem.Raise;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.service.holdem.HoldemWinnerService;
import ru.sm.poker.util.HistoryUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ru.sm.poker.util.DTOUtilTest.DEFAULT_CHIPS_COUNT;
import static ru.sm.poker.util.DTOUtilTest.getPlayer;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class HoldemWinnerServiceTest {

    private static final long firstBet = 200;
    private static final long secondBet = 400;
    private static final long thirdBet = 1000;

    @Autowired
    private HoldemWinnerService holdemWinnerService;

    @Test
    public void testWhenPlayersHaveSameCombinationsAndNoOtherBetsFound() {
        final Player winner1 = getPlayer();
        final Player winner2 = getPlayer();

        winner1.addCards(Arrays.asList(CardType.TWO_H, CardType.THREE_S));
        winner2.addCards(Arrays.asList(CardType.TWO_D, CardType.THREE_D));

        final Map<Player, List<Action>> fullHistory = Map.of(
                winner1, Arrays.asList(new Call(firstBet), new Call(secondBet), new Call(thirdBet)),
                winner2, Arrays.asList(new Raise(firstBet), new Raise(secondBet), new Raise(thirdBet))
        );
        final HoldemRoundSettings roundSettingsDTO = HoldemRoundSettings.builder()
                .flop(Arrays.asList(CardType.TWO_C, CardType.THREE_C, CardType.FOUR_C))
                .tern(CardType.SEVEN_H)
                .river(CardType.TEN_H)
                .bank(firstBet + secondBet + thirdBet * fullHistory.size())
                .fullHistory(fullHistory)
                .players(Arrays.asList(winner1, winner2))
                .build();

        final long firstBets = HistoryUtil.sumRoundHistoryBets(roundSettingsDTO, winner1);
        final long secondBets = HistoryUtil.sumRoundHistoryBets(roundSettingsDTO, winner2);

        winner1.removeChips(firstBets);
        winner2.removeChips(secondBets);

        holdemWinnerService.sendPrizes(roundSettingsDTO, false);

        Assertions.assertEquals(DEFAULT_CHIPS_COUNT, winner1.getChipsCount());
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT, winner2.getChipsCount());
    }

    @Test
    public void testWhenPlayersHaveSameCombinationsAndOtherBetsFound() {
        final Player winner1 = getPlayer();
        final Player winner2 = getPlayer();
        final Player looser = getPlayer();

        winner1.addCards(Arrays.asList(CardType.FIVE_H, CardType.SIX_C));
        winner2.addCards(Arrays.asList(CardType.FIVE_D, CardType.SIX_D));
        looser.addCards(Arrays.asList(CardType.J_D, CardType.TEN_D));

        final Map<Player, List<Action>> fullHistory = Map.of(
                winner1, Arrays.asList(new Call(firstBet), new Call(secondBet), new Call(thirdBet)),
                winner2, Arrays.asList(new Raise(firstBet), new Raise(secondBet), new Raise(thirdBet)),
                looser, Arrays.asList(new Call(firstBet), new Call(secondBet), new Fold())
        );

        final HoldemRoundSettings roundSettingsDTO = HoldemRoundSettings.builder()
                .flop(Arrays.asList(CardType.TWO_C, CardType.THREE_C, CardType.FOUR_C))
                .tern(CardType.SEVEN_H)
                .river(CardType.TEN_H)
                .bank(firstBet + secondBet + thirdBet * fullHistory.size())
                .fullHistory(fullHistory)
                .players(Arrays.asList(winner1, winner2, looser))
                .build();

        final long firstBets = HistoryUtil.sumRoundHistoryBets(roundSettingsDTO, winner1);
        final long secondBets = HistoryUtil.sumRoundHistoryBets(roundSettingsDTO, winner2);
        final long thirdBets = HistoryUtil.sumRoundHistoryBets(roundSettingsDTO, looser);


        final long bank = firstBets + secondBets + thirdBets;

        roundSettingsDTO.setBank(bank);

        winner1.removeChips(firstBets);
        winner2.removeChips(secondBets);
        looser.removeChips(thirdBets);

        holdemWinnerService.sendPrizes(roundSettingsDTO, false);

        Assertions.assertEquals(DEFAULT_CHIPS_COUNT + (thirdBets / 2), winner1.getChipsCount());
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT + (thirdBets / 2), winner2.getChipsCount());
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT - thirdBets, looser.getChipsCount());
    }


    @Test
    public void testWhenPlayersHaveDifferentCombinations() {
        final Player winner = getPlayer();
        final Player looser = getPlayer();

        winner.addCards(Arrays.asList(CardType.K_C, CardType.A_C));
        looser.addCards(Arrays.asList(CardType.TWO_D, CardType.FIVE_H));

        final Map<Player, List<Action>> fullHistory = Map.of(
                winner, Arrays.asList(new Call(firstBet), new Call(secondBet), new Call(thirdBet)),
                looser, Arrays.asList(new Raise(firstBet), new Raise(secondBet), new Raise(thirdBet))
        );

        final HoldemRoundSettings roundSettingsDTO = HoldemRoundSettings.builder()
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

        holdemWinnerService.sendPrizes(roundSettingsDTO, false);

        Assertions.assertEquals(winner.getChipsCount(), DEFAULT_CHIPS_COUNT + firstBets);
        Assertions.assertEquals(looser.getChipsCount(), DEFAULT_CHIPS_COUNT - secondBets);
    }

    @Test
    public void testWhenPlayersHaveSameCombinationsButDifferentPower() {
        final Player winner = getPlayer();
        final Player looser = getPlayer();

        winner.addCards(Arrays.asList(CardType.TWO_H, CardType.TEN_D));
        looser.addCards(Arrays.asList(CardType.TWO_D, CardType.THREE_D));

        final Map<Player, List<Action>> fullHistory = Map.of(
                winner, Arrays.asList(new Call(firstBet), new Call(secondBet), new Call(thirdBet)),
                looser, Arrays.asList(new Raise(firstBet), new Raise(secondBet), new Raise(thirdBet))
        );
        final HoldemRoundSettings roundSettingsDTO = HoldemRoundSettings.builder()
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

        holdemWinnerService.sendPrizes(roundSettingsDTO, false);

        Assertions.assertEquals(winner.getChipsCount(), DEFAULT_CHIPS_COUNT + firstBets);
        Assertions.assertEquals(looser.getChipsCount(), DEFAULT_CHIPS_COUNT - secondBets);
    }

    @Test
    public void testWhenCardsWereNotOpened() {
        final Player winner = getPlayer();
        final Player looser = getPlayer();

        winner.addCards(Arrays.asList(CardType.TWO_H, CardType.TEN_D));
        looser.addCards(Arrays.asList(CardType.TWO_D, CardType.THREE_D));

        final Map<Player, List<Action>> fullHistory = Map.of(
                winner, Arrays.asList(new Call(firstBet), new Call(secondBet)),
                looser, Arrays.asList(new Raise(firstBet), new Raise(secondBet))
        );

        final HoldemRoundSettings roundSettingsDTO = HoldemRoundSettings.builder()
                .flop(Arrays.asList(CardType.TWO_C, CardType.THREE_C, CardType.FOUR_C))
                .fullHistory(fullHistory)
                .players(Arrays.asList(winner, looser))
                .build();

        final long firstBets = HistoryUtil.sumRoundHistoryBets(roundSettingsDTO, winner);
        final long secondBets = HistoryUtil.sumRoundHistoryBets(roundSettingsDTO, looser);

        roundSettingsDTO.setBank(firstBets + secondBets);

        winner.removeChips(firstBets);
        looser.removeChips(secondBets);
        looser.setAction(new Fold());
        holdemWinnerService.sendPrizes(roundSettingsDTO, true);

        Assertions.assertEquals(winner.getChipsCount(), DEFAULT_CHIPS_COUNT + secondBets);
        Assertions.assertEquals(looser.getChipsCount(), DEFAULT_CHIPS_COUNT - secondBets);
    }

}
