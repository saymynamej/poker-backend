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
import ru.smn.poker.dto.ClassicTableSettings;
import ru.smn.poker.entities.CardEntity;
import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.enums.StateType;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.PrizeService;
import ru.smn.poker.util.PlayerUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class SimplePrizeServiceTest {
    @Autowired
    private PrizeService prizeService;

    @Test
    void testWhenWinnerOnlyOne() {
        final long callCount = 100;
        final long raiseCount = 300;
        final PlayerEntity player1 = PlayerUtil.getDefaultPlayerForHoldem("1");
        final PlayerEntity player2 = PlayerUtil.getDefaultPlayerForHoldem("2");
        final Call call = new Call(callCount);
        final Raise raise = new Raise(raiseCount);
        final Fold fold = new Fold();

        player1.getTableSettings().setAction(fold);
        player2.getTableSettings().setAction(call);

        player1.getTableSettings().setStateType(StateType.IN_GAME);
        player2.getTableSettings().setStateType(StateType.IN_GAME);

        prizeService.sendPrizes(ClassicTableSettings.builder()
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

        Assertions.assertEquals(5100, player2.getTableSettings().getChipsCount().getCount());
        Assertions.assertEquals(5000, player1.getTableSettings().getChipsCount().getCount());
    }

    @Test
    void testWhenNeedChop() {
        final PlayerEntity player1 = PlayerUtil.getDefaultPlayerForHoldem("1");
        player1.getTableSettings().addCards(Arrays.asList(
                CardEntity.builder().cardType(CardType.TWO_C).build(),
                CardEntity.builder().cardType(CardType.THREE_H).build())
        );
        final AllIn allIn1 = new AllIn(player1.getTableSettings().getChipsCount().getCount());
        player1.getTableSettings().setAction(allIn1);

        final PlayerEntity player2 = PlayerUtil.getDefaultPlayerForHoldem("2");
        player2.getTableSettings().addCards(Arrays.asList(
                CardEntity.builder().cardType(CardType.TWO_D).build(),
                CardEntity.builder().cardType(CardType.THREE_S).build())
        );
        final AllIn allIn2 = new AllIn(player2.getTableSettings().getChipsCount().getCount());
        player2.getTableSettings().setAction(allIn2);

        player1.getTableSettings().setStateType(StateType.IN_GAME);
        player2.getTableSettings().setStateType(StateType.IN_GAME);

        TableSettings tableSettings = ClassicTableSettings.builder()
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
                .bank(player1.getTableSettings().getChipsCount().getCount() + player2.getTableSettings().getChipsCount().getCount())
                .build();

        player1.getTableSettings().setChipsCount(new ChipsCountEntity());
        player2.getTableSettings().setChipsCount(new ChipsCountEntity());

        prizeService.sendPrizes(tableSettings);

        Assertions.assertEquals(5000, player2.getTableSettings().getChipsCount().getCount());
        Assertions.assertEquals(5000, player1.getTableSettings().getChipsCount().getCount());
    }

    @Test
    void testWhenNeedCalculate() {
        final PlayerEntity player1 = PlayerUtil.getDefaultPlayerForHoldem("1");
        player1.getTableSettings().addCards(
                Arrays.asList(
                        CardEntity.builder().cardType(CardType.K_C).build(),
                        CardEntity.builder().cardType(CardType.K_D).build()
                )
        );
        final AllIn allIn1 = new AllIn(5000);
        player1.getTableSettings().setAction(allIn1);
        player1.getTableSettings().setChipsCount(new ChipsCountEntity());

        final PlayerEntity player2 = PlayerUtil.getDefaultPlayerForHoldem("2");
        player2.getTableSettings().addCards(
                Arrays.asList(
                        CardEntity.builder().cardType(CardType.EIGHT_S).build(),
                        CardEntity.builder().cardType(CardType.NINE_S).build()
                )
        );
        final AllIn allIn2 = new AllIn(2000);
        player2.getTableSettings().setChipsCount(new ChipsCountEntity());
        player2.getTableSettings().setAction(allIn2);

        final PlayerEntity player3 = PlayerUtil.getDefaultPlayerForHoldem("3");
        player3.getTableSettings().addCards(
                Arrays.asList(
                        CardEntity.builder().cardType(CardType.TWO_C).build(),
                        CardEntity.builder().cardType(CardType.THREE_S).build()
                )
        );
        final AllIn allIn3 = new AllIn(100);
        player3.getTableSettings().setChipsCount(new ChipsCountEntity());
        player3.getTableSettings().setAction(allIn3);

        player1.getTableSettings().setStateType(StateType.IN_GAME);
        player2.getTableSettings().setStateType(StateType.IN_GAME);
        player3.getTableSettings().setStateType(StateType.IN_GAME);

        prizeService.sendPrizes(ClassicTableSettings.builder()
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

        Assertions.assertEquals(4100, player2.getTableSettings().getChipsCount().getCount());
        Assertions.assertEquals(3000, player1.getTableSettings().getChipsCount().getCount());
        Assertions.assertEquals(0, player3.getTableSettings().getChipsCount().getCount());
    }

}
