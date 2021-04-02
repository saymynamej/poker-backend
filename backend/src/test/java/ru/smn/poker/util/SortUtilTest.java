package ru.smn.poker.util;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.RoleType;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.service.SortService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class SortUtilTest {

    private final List<PlayerEntity> playersForTestingPreflop = new ArrayList<>();

    private final List<PlayerEntity> playerForTestingPostFlop = new ArrayList<>();

    private final Faker faker = new Faker();

    @Autowired
    private SortService sortService;

    @BeforeEach
    public void before() {
        final PlayerEntity button = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        button.getTableSettings().setButton();
        final PlayerEntity sm = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        sm.getTableSettings().setRole(RoleType.SMALL_BLIND);
        final PlayerEntity bb = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        bb.getTableSettings().setRole(RoleType.BIG_BLIND);
        final PlayerEntity simplePlayer = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        simplePlayer.getTableSettings().setRole(RoleType.ORDINARY);
        playersForTestingPreflop.addAll(Arrays.asList(sm, button, simplePlayer, bb));
        playerForTestingPostFlop.addAll(Arrays.asList(sm, button, simplePlayer, bb));
    }

    @Test
    public void testSortPreflop() {
        final List<PlayerEntity> sortedPlayers = sortService.sort(playersForTestingPreflop, StageType.PREFLOP);

        Assertions.assertEquals(sortedPlayers.get(0).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayers.get(1).getTableSettings().getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayers.get(2).getTableSettings().getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayers.get(3).getTableSettings().getRoleType(), RoleType.BIG_BLIND);

        Collections.shuffle(playersForTestingPreflop);

        final List<PlayerEntity> sortedPlayerWithShuffle = sortService.sort(playersForTestingPreflop, StageType.PREFLOP);

        Assertions.assertEquals(sortedPlayerWithShuffle.get(0).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayerWithShuffle.get(1).getTableSettings().getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayerWithShuffle.get(2).getTableSettings().getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayerWithShuffle.get(3).getTableSettings().getRoleType(), RoleType.BIG_BLIND);

        final PlayerEntity simplePlayer1 = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        simplePlayer1.getTableSettings().setRole(RoleType.ORDINARY);

        final PlayerEntity simplePlayer2 = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        simplePlayer2.getTableSettings().setRole(RoleType.ORDINARY);

        playersForTestingPreflop.addAll(Arrays.asList(simplePlayer1, simplePlayer2));

        final List<PlayerEntity> sortedPlayers2 = sortService.sort(this.playersForTestingPreflop, StageType.PREFLOP);

        Assertions.assertEquals(sortedPlayers2.get(0).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayers2.get(1).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayers2.get(2).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayers2.get(3).getTableSettings().getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayers2.get(4).getTableSettings().getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayers2.get(5).getTableSettings().getRoleType(), RoleType.BIG_BLIND);


        final PlayerEntity simplePlayer3 = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        simplePlayer3.getTableSettings().setRole(RoleType.ORDINARY);

        final PlayerEntity simplePlayer4 = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        simplePlayer4.getTableSettings().setRole(RoleType.ORDINARY);

        final PlayerEntity simplePlayer5 = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        simplePlayer5.getTableSettings().setRole(RoleType.ORDINARY);

        playersForTestingPreflop.addAll(Arrays.asList(simplePlayer3, simplePlayer4, simplePlayer5));

        final List<PlayerEntity> sortedPlayer3 = sortService.sort(this.playersForTestingPreflop, StageType.PREFLOP);

        Assertions.assertEquals(sortedPlayer3.get(0).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayer3.get(1).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayer3.get(2).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayer3.get(3).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayer3.get(4).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayer3.get(5).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayer3.get(6).getTableSettings().getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayer3.get(7).getTableSettings().getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayer3.get(8).getTableSettings().getRoleType(), RoleType.BIG_BLIND);

    }


    @Test
    public void testSortPostFlop() {
        final List<PlayerEntity> players = sortService.sort(playerForTestingPostFlop, StageType.FLOP);

        Assertions.assertEquals(players.get(0).getTableSettings().getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(players.get(1).getTableSettings().getRoleType(), RoleType.BIG_BLIND);
        Assertions.assertEquals(players.get(2).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(players.get(3).getTableSettings().getRoleType(), RoleType.BUTTON);

        final PlayerEntity simplePlayer1 = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        simplePlayer1.getTableSettings().setRole(RoleType.ORDINARY);

        final PlayerEntity simplePlayer2 = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        simplePlayer2.getTableSettings().setRole(RoleType.ORDINARY);


        playerForTestingPostFlop.addAll(Arrays.asList(simplePlayer1, simplePlayer2));

        Collections.shuffle(playerForTestingPostFlop);

        final List<PlayerEntity> sortedPlayersWithShuffle = sortService.sort(playerForTestingPostFlop, StageType.FLOP);


        Assertions.assertEquals(sortedPlayersWithShuffle.get(0).getTableSettings().getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(1).getTableSettings().getRoleType(), RoleType.BIG_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(2).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(3).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(4).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(5).getTableSettings().getRoleType(), RoleType.BUTTON);


        final PlayerEntity simplePlayer3 = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        simplePlayer3.getTableSettings().setRole(RoleType.ORDINARY);

        final PlayerEntity simplePlayer4 = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        simplePlayer4.getTableSettings().setRole(RoleType.ORDINARY);

        final PlayerEntity simplePlayer5 = PlayerUtil.getDefaultPlayerForHoldem(faker.name().name());
        simplePlayer5.getTableSettings().setRole(RoleType.ORDINARY);

        playerForTestingPostFlop.addAll(Arrays.asList(simplePlayer3, simplePlayer4, simplePlayer5));

        Collections.shuffle(playerForTestingPostFlop);

        final List<PlayerEntity> sortedPlayersWithShuffle2 = sortService.sort(playerForTestingPostFlop, StageType.FLOP);


        Assertions.assertEquals(sortedPlayersWithShuffle2.get(0).getTableSettings().getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(1).getTableSettings().getRoleType(), RoleType.BIG_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(2).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(3).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(4).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(5).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(6).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(7).getTableSettings().getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(8).getTableSettings().getRoleType(), RoleType.BUTTON);

    }

    @Test
    public void loadTestShufflingPreflop() {

        for (int i = 0; i < 1_000_000; i++) {
            Collections.shuffle(playersForTestingPreflop);

            final List<PlayerEntity> players = sortService.sort(playersForTestingPreflop, StageType.PREFLOP);

            Assertions.assertEquals(players.get(0).getTableSettings().getRoleType(), RoleType.ORDINARY);
            Assertions.assertEquals(players.get(1).getTableSettings().getRoleType(), RoleType.BUTTON);
            Assertions.assertEquals(players.get(2).getTableSettings().getRoleType(), RoleType.SMALL_BLIND);
            Assertions.assertEquals(players.get(3).getTableSettings().getRoleType(), RoleType.BIG_BLIND);

        }
    }

    @Test
    public void loadTestShufflingPostFlop() {
        for (int i = 0; i < 1_000_000; i++) {
            Collections.shuffle(playerForTestingPostFlop);
            final List<PlayerEntity> players = sortService.sort(playerForTestingPostFlop, StageType.FLOP);
            Assertions.assertEquals(players.get(0).getTableSettings().getRoleType(), RoleType.SMALL_BLIND);
            Assertions.assertEquals(players.get(1).getTableSettings().getRoleType(), RoleType.BIG_BLIND);
            Assertions.assertEquals(players.get(2).getTableSettings().getRoleType(), RoleType.ORDINARY);
            Assertions.assertEquals(players.get(3).getTableSettings().getRoleType(), RoleType.BUTTON);
        }
    }
}

