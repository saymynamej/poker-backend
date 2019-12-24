package ru.sm.poker.util;

import com.github.javafaker.Faker;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortUtilTest {

    private final List<Player> playersForTestingPreflop = new ArrayList<>();

    private final List<Player> playerForTestingPostFlop = new ArrayList<>();

    private final Faker faker = new Faker();

    @Before
    public void before() {
        final Player button = new Player(faker.name().name(), 5000);
        button.setButton();
        final Player sm = new Player(faker.name().name(), 5000);
        sm.setRole(RoleType.SMALL_BLIND);
        final Player bb = new Player(faker.name().name(), 5000);
        bb.setRole(RoleType.BIG_BLIND);
        final Player simplePlayer = new Player(faker.name().name(), 5000);
        simplePlayer.setRole(RoleType.PLAYER);
        playersForTestingPreflop.addAll(Arrays.asList(sm, button, simplePlayer, bb));
        playerForTestingPostFlop.addAll(Arrays.asList(sm, button, simplePlayer, bb));
    }

    @Test
    public void testSortPreflop() {
        final List<Player> sortedPlayers = SortUtil.sortPreflop(playersForTestingPreflop);

        Assertions.assertEquals(sortedPlayers.get(0).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayers.get(1).getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayers.get(2).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayers.get(3).getRoleType(), RoleType.BIG_BLIND);

        Collections.shuffle(playersForTestingPreflop);

        final List<Player> sortedPlayerWithShuffle = SortUtil.sortPreflop(playersForTestingPreflop);

        Assertions.assertEquals(sortedPlayerWithShuffle.get(0).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayerWithShuffle.get(1).getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayerWithShuffle.get(2).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayerWithShuffle.get(3).getRoleType(), RoleType.BIG_BLIND);

        final Player simplePlayer1 = new Player(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.PLAYER);

        final Player simplePlayer2 = new Player(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.PLAYER);

        playersForTestingPreflop.addAll(Arrays.asList(simplePlayer1, simplePlayer2));

        List<Player> sortedPlayers2 = SortUtil.sortPreflop(this.playersForTestingPreflop);

        Assertions.assertEquals(sortedPlayers2.get(0).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayers2.get(1).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayers2.get(2).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayers2.get(3).getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayers2.get(4).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayers2.get(5).getRoleType(), RoleType.BIG_BLIND);


        final Player simplePlayer3 = new Player(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.PLAYER);

        final Player simplePlayer4 = new Player(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.PLAYER);

        final Player simplePlayer5 = new Player(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.PLAYER);

        playersForTestingPreflop.addAll(Arrays.asList(simplePlayer3, simplePlayer4, simplePlayer5));

        final List<Player> sortedPlayer3 = SortUtil.sortPreflop(this.playersForTestingPreflop);

        Assertions.assertEquals(sortedPlayer3.get(0).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayer3.get(1).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayer3.get(2).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayer3.get(3).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayer3.get(4).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayer3.get(5).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayer3.get(6).getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayer3.get(7).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayer3.get(8).getRoleType(), RoleType.BIG_BLIND);

    }


    @Test
    public void testSortPostFlop() {
        final List<Player> players = SortUtil.sortPostflop(playerForTestingPostFlop);

        Assertions.assertEquals(players.get(0).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(players.get(1).getRoleType(), RoleType.BIG_BLIND);
        Assertions.assertEquals(players.get(2).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(players.get(3).getRoleType(), RoleType.BUTTON);

        final Player simplePlayer1 = new Player(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.PLAYER);

        final Player simplePlayer2 = new Player(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.PLAYER);


        playerForTestingPostFlop.addAll(Arrays.asList(simplePlayer1, simplePlayer2));

        Collections.shuffle(playerForTestingPostFlop);

        final List<Player> sortedPlayersWithShuffle = SortUtil.sortPostflop(playerForTestingPostFlop);


        Assertions.assertEquals(sortedPlayersWithShuffle.get(0).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(1).getRoleType(), RoleType.BIG_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(2).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(3).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(4).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(5).getRoleType(), RoleType.BUTTON);


        final Player simplePlayer3 = new Player(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.PLAYER);

        final Player simplePlayer4 = new Player(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.PLAYER);

        final Player simplePlayer5 = new Player(faker.name().name(), 5000);
        simplePlayer1.setRole(RoleType.PLAYER);

        playerForTestingPostFlop.addAll(Arrays.asList(simplePlayer3, simplePlayer4, simplePlayer5));

        Collections.shuffle(playerForTestingPostFlop);

        final List<Player> sortedPlayersWithShuffle2 = SortUtil.sortPostflop(playerForTestingPostFlop);


        Assertions.assertEquals(sortedPlayersWithShuffle2.get(0).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(1).getRoleType(), RoleType.BIG_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(2).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(3).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(4).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(5).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(6).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(7).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayersWithShuffle2.get(8).getRoleType(), RoleType.BUTTON);

    }

    @Test
    public void loadTestShufflingPreflop(){

        for (int i = 0; i < 1_000_000; i++) {
            Collections.shuffle(playersForTestingPreflop);

            final List<Player> players = SortUtil.sortPreflop(playersForTestingPreflop);

            Assertions.assertEquals(players.get(0).getRoleType(), RoleType.PLAYER);
            Assertions.assertEquals(players.get(1).getRoleType(), RoleType.BUTTON);
            Assertions.assertEquals(players.get(2).getRoleType(), RoleType.SMALL_BLIND);
            Assertions.assertEquals(players.get(3).getRoleType(), RoleType.BIG_BLIND);

        }
    }

    @Test
    public void loadTestShufflingPostFlop(){

        for (int i = 0; i < 1_000_000; i++) {
            Collections.shuffle(playerForTestingPostFlop);

            final List<Player> players = SortUtil.sortPostflop(playerForTestingPostFlop);

            Assertions.assertEquals(players.get(0).getRoleType(), RoleType.SMALL_BLIND);
            Assertions.assertEquals(players.get(1).getRoleType(), RoleType.BIG_BLIND);
            Assertions.assertEquals(players.get(2).getRoleType(), RoleType.PLAYER);
            Assertions.assertEquals(players.get(3).getRoleType(), RoleType.BUTTON);

        }
    }
}