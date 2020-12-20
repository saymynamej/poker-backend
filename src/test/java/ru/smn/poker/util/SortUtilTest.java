//package ru.smn.poker.util;
//
//import com.github.javafaker.Faker;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import ru.smn.poker.dto.Player;
//import ru.smn.poker.entities.PlayerEntity;
//import ru.smn.poker.enums.RoleType;
//import ru.smn.poker.enums.StageType;
//import ru.smn.poker.service.SortService;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@ActiveProfiles("test")
//public class SortUtilTest {
//
//    private final List<PlayerEntity> playersForTestingPreflop = new ArrayList<>();
//
//    private final List<PlayerEntity> playerForTestingPostFlop = new ArrayList<>();
//
//    private final Faker faker = new Faker();
//
//    @Autowired
//    private SortService sortService;
//
//    @Before
//    public void before() {
//        final PlayerEntity button = new PlayerEntity(faker.name().name(), 5000);
//        button.setButton();
//        final PlayerEntity sm = new Player(faker.name().name(), 5000);
//        sm.setRole(RoleType.SMALL_BLIND);
//        final PlayerEntity bb = new Player(faker.name().name(), 5000);
//        bb.setRole(RoleType.BIG_BLIND);
//        final PlayerEntity simplePlayer = new Player(faker.name().name(), 5000);
//        simplePlayer.setRole(RoleType.ORDINARY);
//        playersForTestingPreflop.addAll(Arrays.asList(sm, button, simplePlayer, bb));
//        playerForTestingPostFlop.addAll(Arrays.asList(sm, button, simplePlayer, bb));
//    }
//
//    @Test
//    public void testSortPreflop() {
//        final List<Player> sortedPlayers = sortService.sort(playersForTestingPreflop, StageType.PREFLOP);
//
//        Assertions.assertEquals(sortedPlayers.get(0).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayers.get(1).getRoleType(), RoleType.BUTTON);
//        Assertions.assertEquals(sortedPlayers.get(2).getRoleType(), RoleType.SMALL_BLIND);
//        Assertions.assertEquals(sortedPlayers.get(3).getRoleType(), RoleType.BIG_BLIND);
//
//        Collections.shuffle(playersForTestingPreflop);
//
//        final List<Player> sortedPlayerWithShuffle = SortUtil.sortPreflop(playersForTestingPreflop);
//
//        Assertions.assertEquals(sortedPlayerWithShuffle.get(0).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayerWithShuffle.get(1).getRoleType(), RoleType.BUTTON);
//        Assertions.assertEquals(sortedPlayerWithShuffle.get(2).getRoleType(), RoleType.SMALL_BLIND);
//        Assertions.assertEquals(sortedPlayerWithShuffle.get(3).getRoleType(), RoleType.BIG_BLIND);
//
//        final Player simplePlayer1 = new Player(faker.name().name(), 5000);
//        simplePlayer1.setRole(RoleType.ORDINARY);
//
//        final Player simplePlayer2 = new Player(faker.name().name(), 5000);
//        simplePlayer2.setRole(RoleType.ORDINARY);
//
//        playersForTestingPreflop.addAll(Arrays.asList(simplePlayer1, simplePlayer2));
//
//        final List<Player> sortedPlayers2 = SortUtil.sortPreflop(this.playersForTestingPreflop);
//
//        Assertions.assertEquals(sortedPlayers2.get(0).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayers2.get(1).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayers2.get(2).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayers2.get(3).getRoleType(), RoleType.BUTTON);
//        Assertions.assertEquals(sortedPlayers2.get(4).getRoleType(), RoleType.SMALL_BLIND);
//        Assertions.assertEquals(sortedPlayers2.get(5).getRoleType(), RoleType.BIG_BLIND);
//
//
//        final Player simplePlayer3 = new Player(faker.name().name(), 5000);
//        simplePlayer3.setRole(RoleType.ORDINARY);
//
//        final Player simplePlayer4 = new Player(faker.name().name(), 5000);
//        simplePlayer4.setRole(RoleType.ORDINARY);
//
//        final Player simplePlayer5 = new Player(faker.name().name(), 5000);
//        simplePlayer5.setRole(RoleType.ORDINARY);
//
//        playersForTestingPreflop.addAll(Arrays.asList(simplePlayer3, simplePlayer4, simplePlayer5));
//
//        final List<Player> sortedPlayer3 = SortUtil.sortPreflop(this.playersForTestingPreflop);
//
//        Assertions.assertEquals(sortedPlayer3.get(0).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayer3.get(1).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayer3.get(2).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayer3.get(3).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayer3.get(4).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayer3.get(5).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayer3.get(6).getRoleType(), RoleType.BUTTON);
//        Assertions.assertEquals(sortedPlayer3.get(7).getRoleType(), RoleType.SMALL_BLIND);
//        Assertions.assertEquals(sortedPlayer3.get(8).getRoleType(), RoleType.BIG_BLIND);
//
//    }
//
//
//    @Test
//    public void testSortPostFlop() {
//        final List<PlayerEntity> players = sortService.sort(playerForTestingPostFlop, StageType.FLOP);
//
//        Assertions.assertEquals(players.get(0).getRoleType(), RoleType.SMALL_BLIND);
//        Assertions.assertEquals(players.get(1).getRoleType(), RoleType.BIG_BLIND);
//        Assertions.assertEquals(players.get(2).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(players.get(3).getRoleType(), RoleType.BUTTON);
//
//        final Player simplePlayer1 = new Player(faker.name().name(), 5000);
//        simplePlayer1.setRole(RoleType.ORDINARY);
//
//        final Player simplePlayer2 = new Player(faker.name().name(), 5000);
//        simplePlayer2.setRole(RoleType.ORDINARY);
//
//
//        playerForTestingPostFlop.addAll(Arrays.asList(simplePlayer1, simplePlayer2));
//
//        Collections.shuffle(playerForTestingPostFlop);
//
//        final List<Player> sortedPlayersWithShuffle = sortService.sort(playerForTestingPostFlop);
//
//
//        Assertions.assertEquals(sortedPlayersWithShuffle.get(0).getRoleType(), RoleType.SMALL_BLIND);
//        Assertions.assertEquals(sortedPlayersWithShuffle.get(1).getRoleType(), RoleType.BIG_BLIND);
//        Assertions.assertEquals(sortedPlayersWithShuffle.get(2).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayersWithShuffle.get(3).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayersWithShuffle.get(4).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayersWithShuffle.get(5).getRoleType(), RoleType.BUTTON);
//
//
//        final Player simplePlayer3 = new Player(faker.name().name(), 5000);
//        simplePlayer3.setRole(RoleType.ORDINARY);
//
//        final Player simplePlayer4 = new Player(faker.name().name(), 5000);
//        simplePlayer4.setRole(RoleType.ORDINARY);
//
//        final Player simplePlayer5 = new Player(faker.name().name(), 5000);
//        simplePlayer5.setRole(RoleType.ORDINARY);
//
//        playerForTestingPostFlop.addAll(Arrays.asList(simplePlayer3, simplePlayer4, simplePlayer5));
//
//        Collections.shuffle(playerForTestingPostFlop);
//
//        final List<Player> sortedPlayersWithShuffle2 = SortUtil.sortPostflop(playerForTestingPostFlop);
//
//
//        Assertions.assertEquals(sortedPlayersWithShuffle2.get(0).getRoleType(), RoleType.SMALL_BLIND);
//        Assertions.assertEquals(sortedPlayersWithShuffle2.get(1).getRoleType(), RoleType.BIG_BLIND);
//        Assertions.assertEquals(sortedPlayersWithShuffle2.get(2).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayersWithShuffle2.get(3).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayersWithShuffle2.get(4).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayersWithShuffle2.get(5).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayersWithShuffle2.get(6).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayersWithShuffle2.get(7).getRoleType(), RoleType.ORDINARY);
//        Assertions.assertEquals(sortedPlayersWithShuffle2.get(8).getRoleType(), RoleType.BUTTON);
//
//    }
//
//    @Test
//    public void loadTestShufflingPreflop(){
//
//        for (int i = 0; i < 1_000_000; i++) {
//            Collections.shuffle(playersForTestingPreflop);
//
//            final List<PlayerEntity> players = sortService.sort(playersForTestingPreflop, StageType.PREFLOP);
//
//            Assertions.assertEquals(players.get(0).getRoleType(), RoleType.ORDINARY);
//            Assertions.assertEquals(players.get(1).getRoleType(), RoleType.BUTTON);
//            Assertions.assertEquals(players.get(2).getRoleType(), RoleType.SMALL_BLIND);
//            Assertions.assertEquals(players.get(3).getRoleType(), RoleType.BIG_BLIND);
//
//        }
//    }
//
//    @Test
//    public void loadTestShufflingPostFlop(){
//
//        for (int i = 0; i < 1_000_000; i++) {
//            Collections.shuffle(playerForTestingPostFlop);
//
//            final List<PlayerEntity> players = sortService.sort(playerForTestingPostFlop, StageType.FLOP);
//
//            Assertions.assertEquals(players.get(0).getRoleType(), RoleType.SMALL_BLIND);
//            Assertions.assertEquals(players.get(1).getRoleType(), RoleType.BIG_BLIND);
//            Assertions.assertEquals(players.get(2).getRoleType(), RoleType.ORDINARY);
//            Assertions.assertEquals(players.get(3).getRoleType(), RoleType.BUTTON);
//
//        }
//    }
//}
