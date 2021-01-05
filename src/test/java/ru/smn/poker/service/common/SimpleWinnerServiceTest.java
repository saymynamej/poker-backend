package ru.smn.poker.service.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.smn.poker.action.holdem.AllIn;
import ru.smn.poker.action.holdem.Call;
import ru.smn.poker.action.holdem.Fold;
import ru.smn.poker.action.holdem.Raise;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.entities.CardEntity;
import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.enums.StateType;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.service.WinnerService;
import ru.smn.poker.util.PlayerUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class SimpleWinnerServiceTest {
    @Autowired
    private WinnerService winnerService;

    @Test
    void testWhenWinnerOnlyOne() {
        final long callCount = 100;
        final long raiseCount = 300;
        final PlayerEntity player1 = PlayerUtil.getDefaultPlayerForHoldem("1");
        final PlayerEntity player2 = PlayerUtil.getDefaultPlayerForHoldem("2");
        final Call call = new Call(callCount);
        final Raise raise = new Raise(raiseCount);
        final Fold fold = new Fold();

        player1.setAction(fold);
        player2.setAction(call);

        player1.setStateType(StateType.IN_GAME);
        player2.setStateType(StateType.IN_GAME);

        winnerService.sendPrizes(HoldemRoundSettings.builder()
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

        Assertions.assertEquals(5100, player2.getChipsCount().getCount());
        Assertions.assertEquals(5000, player1.getChipsCount().getCount());
    }

    @Test
    void testWhenNeedChop() {
        final PlayerEntity player1 = PlayerUtil.getDefaultPlayerForHoldem("1");
        player1.addCards(Arrays.asList(
                CardEntity.builder().cardType(CardType.TWO_C).build(),
                CardEntity.builder().cardType(CardType.THREE_H).build())
        );
        final AllIn allIn1 = new AllIn(player1.getChipsCount().getCount());
        player1.setAction(allIn1);

        final PlayerEntity player2 = PlayerUtil.getDefaultPlayerForHoldem("2");
        player2.addCards(Arrays.asList(
                CardEntity.builder().cardType(CardType.TWO_D).build(),
                CardEntity.builder().cardType(CardType.THREE_S).build())
        );
        final AllIn allIn2 = new AllIn(player2.getChipsCount().getCount());
        player2.setAction(allIn2);

        player1.setStateType(StateType.IN_GAME);
        player2.setStateType(StateType.IN_GAME);

        RoundSettings roundSettings = HoldemRoundSettings.builder()
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
                .bank(player1.getChipsCount().getCount() + player2.getChipsCount().getCount())
                .build();

        player1.setChipsCount(new ChipsCountEntity());
        player2.setChipsCount(new ChipsCountEntity());

        winnerService.sendPrizes(roundSettings);

        Assertions.assertEquals(5000, player2.getChipsCount().getCount());
        Assertions.assertEquals(5000, player1.getChipsCount().getCount());
    }

    @Test
    void testWhenNeedCalculate() {
        final PlayerEntity player1 = PlayerUtil.getDefaultPlayerForHoldem("1");
        player1.addCards(
                Arrays.asList(
                        CardEntity.builder().cardType(CardType.K_C).build(),
                        CardEntity.builder().cardType(CardType.K_D).build()
                )
        );
        final AllIn allIn1 = new AllIn(5000);
        player1.setAction(allIn1);
        player1.setChipsCount(new ChipsCountEntity());

        final PlayerEntity player2 = PlayerUtil.getDefaultPlayerForHoldem("2");
        player2.addCards(
                Arrays.asList(
                        CardEntity.builder().cardType(CardType.EIGHT_S).build(),
                        CardEntity.builder().cardType(CardType.NINE_S).build()
                )
        );
        final AllIn allIn2 = new AllIn(2000);
        player2.setChipsCount(new ChipsCountEntity());
        player2.setAction(allIn2);

        final PlayerEntity player3 = PlayerUtil.getDefaultPlayerForHoldem("3");
        player3.addCards(
                Arrays.asList(
                        CardEntity.builder().cardType(CardType.TWO_C).build(),
                        CardEntity.builder().cardType(CardType.THREE_S).build()
                )
        );
        final AllIn allIn3 = new AllIn(100);
        player3.setChipsCount(new ChipsCountEntity());
        player3.setAction(allIn3);

        player1.setStateType(StateType.IN_GAME);
        player2.setStateType(StateType.IN_GAME);
        player3.setStateType(StateType.IN_GAME);

        winnerService.sendPrizes(HoldemRoundSettings.builder()
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

        Assertions.assertEquals(4100, player2.getChipsCount().getCount());
        Assertions.assertEquals(3000, player1.getChipsCount().getCount());
        Assertions.assertEquals(0, player3.getChipsCount().getCount());
    }

}
