package ru.sm.poker.service.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sm.poker.action.holdem.AllIn;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.action.holdem.Fold;
import ru.sm.poker.action.holdem.Raise;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.service.WinnerService;
import ru.sm.poker.service.holdem.HoldemCombinationService;
import ru.sm.poker.util.PlayerUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


class SimpleWinnerServiceTest {
    private final WinnerService winnerService = new SimpleWinnerService(new HoldemCombinationService());

    @Test
    void testWhenWinnerOnlyOne() {
        final long callCount = 100;
        final long raiseCount = 300;
        final PlayerDTO player1 = PlayerUtil.getDefaultPlayerForHoldem("1");
        final PlayerDTO player2 = PlayerUtil.getDefaultPlayerForHoldem("2");
        final Call call = new Call(callCount);
        final Raise raise = new Raise(raiseCount);
        final Fold fold = new Fold();

        player1.setAction(fold);
        player2.setAction(call);

        player1.setStateType(StateType.IN_GAME);
        player2.setStateType(StateType.IN_GAME);

        winnerService.sendPrizes(HoldemRoundSettingsDTO.builder()
                .fullHistory(Map.of(
                        player1, List.of(call),
                        player2, List.of(call, raise)
                ))
                .players(Arrays.asList(
                        player1,
                        player2
                ))
                .bank(callCount)
                .build());

        Assertions.assertEquals(5100, player2.getChipsCount());
        Assertions.assertEquals(5000, player1.getChipsCount());
    }

    @Test
    void testWhenNeedChop() {
        final PlayerDTO player1 = PlayerUtil.getDefaultPlayerForHoldem("1");
        player1.addCards(Arrays.asList(CardType.TWO_C, CardType.THREE_H));
        final AllIn allIn1 = new AllIn(player1.getChipsCount());
        player1.setAction(allIn1);

        final PlayerDTO player2 = PlayerUtil.getDefaultPlayerForHoldem("2");
        player2.addCards(Arrays.asList(CardType.TWO_D, CardType.THREE_S));
        final AllIn allIn2 = new AllIn(player2.getChipsCount());
        player2.setAction(allIn2);

        player1.setStateType(StateType.IN_GAME);
        player2.setStateType(StateType.IN_GAME);

        HoldemRoundSettingsDTO holdemRoundSettings = HoldemRoundSettingsDTO.builder()
                .flop(Arrays.asList(
                        CardType.FOUR_C,
                        CardType.FIVE_D,
                        CardType.SIX_C)
                )
                .tern(CardType.A_C)
                .river(CardType.A_D)
                .fullHistory(Map.of(
                        player1, List.of(allIn1),
                        player2, List.of(allIn2)
                ))
                .players(Arrays.asList(
                        player1,
                        player2
                ))
                .bank(player1.getChipsCount() + player2.getChipsCount())
                .build();

        player1.setChipsCount(0);
        player2.setChipsCount(0);

        winnerService.sendPrizes(holdemRoundSettings);

        Assertions.assertEquals(5000, player2.getChipsCount());
        Assertions.assertEquals(5000, player1.getChipsCount());
    }

    @Test
    void testWhenNeedCalculate() {
        final PlayerDTO player1 = PlayerUtil.getDefaultPlayerForHoldem("1");
        player1.addCards(Arrays.asList(CardType.K_C, CardType.K_D));
        final AllIn allIn1 = new AllIn(5000);
        player1.setAction(allIn1);
        player1.setChipsCount(0);

        final PlayerDTO player2 = PlayerUtil.getDefaultPlayerForHoldem("2");
        player2.addCards(Arrays.asList(CardType.EIGHT_S, CardType.NINE_S));
        final AllIn allIn2 = new AllIn(2000);
        player2.setChipsCount(0);
        player2.setAction(allIn2);

        final PlayerDTO player3 = PlayerUtil.getDefaultPlayerForHoldem("3");
        player3.addCards(Arrays.asList(CardType.TWO_C, CardType.THREE_S));
        final AllIn allIn3 = new AllIn(100);
        player3.setChipsCount(0);
        player3.setAction(allIn3);

        player1.setStateType(StateType.IN_GAME);
        player2.setStateType(StateType.IN_GAME);
        player3.setStateType(StateType.IN_GAME);

        winnerService.sendPrizes(HoldemRoundSettingsDTO.builder()
                .flop(Arrays.asList(
                        CardType.Q_S,
                        CardType.TWO_D,
                        CardType.THREE_C)
                )
                .tern(CardType.J_S)
                .river(CardType.TEN_S)
                .fullHistory(Map.of(
                        player1, List.of(allIn1),
                        player2, List.of(allIn2),
                        player3, List.of(allIn3)
                ))
                .players(Arrays.asList(
                        player1,
                        player2,
                        player3
                ))
                .bank(7100)
                .build());

        Assertions.assertEquals(4100, player2.getChipsCount());
        Assertions.assertEquals(3000, player1.getChipsCount());
        Assertions.assertEquals(0, player3.getChipsCount());
    }

}
