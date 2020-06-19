package ru.sm.poker.util;

import com.github.javafaker.Faker;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.RoleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortUtilTest {

    private final List<PlayerDTO> playersForTestingPreflop = new ArrayList<>();

    private final List<PlayerDTO> playerForTestingPostFlop = new ArrayList<>();

    private final Faker faker = new Faker();


    @Before
    public void before() {
        final PlayerDTO button = new PlayerDTO(faker.name().name(), 5000);
        button.setButton();
        final PlayerDTO sm = new PlayerDTO(faker.name().name(), 5000);
        sm.setRole(RoleType.SMALL_BLIND);
        final PlayerDTO bb = new PlayerDTO(faker.name().name(), 5000);
        bb.setRole(RoleType.BIG_BLIND);
        final PlayerDTO simplePlayer = new PlayerDTO(faker.name().name(), 5000);
        simplePlayer.setRole(RoleType.ORDINARY);
        playersForTestingPreflop.addAll(Arrays.asList(sm, button, simplePlayer, bb));
        playerForTestingPostFlop.addAll(Arrays.asList(sm, button, simplePlayer, bb));
    }

    @Test
    public void testSortPreflop() {
        final List<PlayerDTO> sortedPlayers = SortUtil.sortPreflop(playersForTestingPreflop);

        Assertions.assertEquals(sortedPlayers.get(0).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayers.get(1).getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayers.get(2).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayers.get(3).getRoleType(), RoleType.BIG_BLIND);

        Collections.shuffle(playersForTestingPreflop);

        final List<PlayerDTO> sortedPlayerWithShuffle = SortUtil.sortPreflop(playersForTestingPreflop);

        Assertions.assertEquals(sortedPlayerWithShuffle.get(0).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayerWithShuffle.get(1).getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayerWithShuffle.get(2).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayerWithShuffle.get(3).getRoleType(), RoleType.BIG_BLIND);

        final PlayerDTO simplePlayer1 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.ORDINARY);

        final PlayerDTO simplePlayer2 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.ORDINARY);

        playersForTestingPreflop.addAll(Arrays.asList(simplePlayer1, simplePlayer2));

        List<PlayerDTO> sortedPlayers2 = SortUtil.sortPreflop(this.playersForTestingPreflop);

        Assertions.assertEquals(sortedPlayers2.get(0).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayers2.get(1).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayers2.get(2).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayers2.get(3).getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayers2.get(4).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayers2.get(5).getRoleType(), RoleType.BIG_BLIND);


        final PlayerDTO simplePlayer3 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.ORDINARY);

        final PlayerDTO simplePlayer4 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.ORDINARY);

        final PlayerDTO simplePlayer5 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.ORDINARY);

        playersForTestingPreflop.addAll(Arrays.asList(simplePlayer3, simplePlayer4, simplePlayer5));

        final List<PlayerDTO> sortedPlayer3 = SortUtil.sortPreflop(this.playersForTestingPreflop);

        Assertions.assertEquals(sortedPlayer3.get(0).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayer3.get(1).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayer3.get(2).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayer3.get(3).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayer3.get(4).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayer3.get(5).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayer3.get(6).getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayer3.get(7).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayer3.get(8).getRoleType(), RoleType.BIG_BLIND);

    }


    @Test
    public void testSortPostFlop() {
        final List<PlayerDTO> players = SortUtil.sortPostflop(playerForTestingPostFlop);

        Assertions.assertEquals(players.get(0).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(players.get(1).getRoleType(), RoleType.BIG_BLIND);
        Assertions.assertEquals(players.get(2).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(players.get(3).getRoleType(), RoleType.BUTTON);

        final PlayerDTO simplePlayer1 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.ORDINARY);

        final PlayerDTO simplePlayer2 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.ORDINARY);


        playerForTestingPostFlop.addAll(Arrays.asList(simplePlayer1, simplePlayer2));

        Collections.shuffle(playerForTestingPostFlop);

        final List<PlayerDTO> sortedPlayersWithShuffle = SortUtil.sortPostflop(playerForTestingPostFlop);


        Assertions.assertEquals(sortedPlayersWithShuffle.get(0).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(1).getRoleType(), RoleType.BIG_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(2).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(3).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(4).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(5).getRoleType(), RoleType.BUTTON);


        final PlayerDTO simplePlayer3 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.ORDINARY);

        final PlayerDTO simplePlayer4 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.ORDINARY);

        final PlayerDTO simplePlayer5 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.ORDINARY);

        playerForTestingPostFlop.addAll(Arrays.asList(simplePlayer3, simplePlayer4, simplePlayer5));

        Collections.shuffle(playerForTestingPostFlop);

        final List<PlayerDTO> sortedPlayersWithShuffle2 = SortUtil.sortPostflop(playerForTestingPostFlop);


        Assertions.assertEquals(sortedPlayersWithShuffle2.get(0).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(1).getRoleType(), RoleType.BIG_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(2).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(3).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(4).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(5).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(6).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(7).getRoleType(), RoleType.ORDINARY);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(8).getRoleType(), RoleType.BUTTON);

    }

    @Test
    public void loadTestShufflingPreflop(){

        for (int i = 0; i < 1_000_000; i++) {
            Collections.shuffle(playersForTestingPreflop);

            final List<PlayerDTO> players = SortUtil.sortPreflop(playersForTestingPreflop);

            Assertions.assertEquals(players.get(0).getRoleType(), RoleType.ORDINARY);
            Assertions.assertEquals(players.get(1).getRoleType(), RoleType.BUTTON);
            Assertions.assertEquals(players.get(2).getRoleType(), RoleType.SMALL_BLIND);
            Assertions.assertEquals(players.get(3).getRoleType(), RoleType.BIG_BLIND);

        }
    }

    @Test
    public void loadTestShufflingPostFlop(){

        for (int i = 0; i < 1_000_000; i++) {
            Collections.shuffle(playerForTestingPostFlop);

            final List<PlayerDTO> players = SortUtil.sortPostflop(playerForTestingPostFlop);

            Assertions.assertEquals(players.get(0).getRoleType(), RoleType.SMALL_BLIND);
            Assertions.assertEquals(players.get(1).getRoleType(), RoleType.BIG_BLIND);
            Assertions.assertEquals(players.get(2).getRoleType(), RoleType.ORDINARY);
            Assertions.assertEquals(players.get(3).getRoleType(), RoleType.BUTTON);

        }
    }
}
