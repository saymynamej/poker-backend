package ru.sm.poker.util;

import com.github.javafaker.Faker;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.dto.PlayerDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortUtilTest {

    private final List<PlayerDTO> playersForTestingPreflop = new ArrayList<>();

    private final List<PlayerDTO> playerDTOForTestingPostFlop = new ArrayList<>();

    private final Faker faker = new Faker();

    @Before
    public void before() {
        final PlayerDTO button = new PlayerDTO(faker.name().name(), 5000);
        button.setButton();
        final PlayerDTO sm = new PlayerDTO(faker.name().name(), 5000);
        sm.setRole(RoleType.SMALL_BLIND);
        final PlayerDTO bb = new PlayerDTO(faker.name().name(), 5000);
        bb.setRole(RoleType.BIG_BLIND);
        final PlayerDTO simplePlayerDTO = new PlayerDTO(faker.name().name(), 5000);
        simplePlayerDTO.setRole(RoleType.PLAYER);
        playersForTestingPreflop.addAll(Arrays.asList(sm, button, simplePlayerDTO, bb));
        playerDTOForTestingPostFlop.addAll(Arrays.asList(sm, button, simplePlayerDTO, bb));
    }

    @Test
    public void testSortPreflop() {
        final List<PlayerDTO> sortedPlayerDTOS = SortUtil.sortPreflop(playersForTestingPreflop);

        Assertions.assertEquals(sortedPlayerDTOS.get(0).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayerDTOS.get(1).getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayerDTOS.get(2).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayerDTOS.get(3).getRoleType(), RoleType.BIG_BLIND);

        Collections.shuffle(playersForTestingPreflop);

        final List<PlayerDTO> sortedPlayerDTOWithShuffle = SortUtil.sortPreflop(playersForTestingPreflop);

        Assertions.assertEquals(sortedPlayerDTOWithShuffle.get(0).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayerDTOWithShuffle.get(1).getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayerDTOWithShuffle.get(2).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayerDTOWithShuffle.get(3).getRoleType(), RoleType.BIG_BLIND);

        final PlayerDTO simplePlayerDTO1 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayerDTO1.setRole(RoleType.PLAYER);

        final PlayerDTO simplePlayerDTO2 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayerDTO1.setRole(RoleType.PLAYER);

        playersForTestingPreflop.addAll(Arrays.asList(simplePlayerDTO1, simplePlayerDTO2));

        List<PlayerDTO> sortedPlayers2 = SortUtil.sortPreflop(this.playersForTestingPreflop);

        Assertions.assertEquals(sortedPlayers2.get(0).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayers2.get(1).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayers2.get(2).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayers2.get(3).getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayers2.get(4).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayers2.get(5).getRoleType(), RoleType.BIG_BLIND);


        final PlayerDTO simplePlayerDTO3 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayerDTO1.setRole(RoleType.PLAYER);

        final PlayerDTO simplePlayerDTO4 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayerDTO1.setRole(RoleType.PLAYER);

        final PlayerDTO simplePlayerDTO5 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayerDTO1.setRole(RoleType.PLAYER);

        playersForTestingPreflop.addAll(Arrays.asList(simplePlayerDTO3, simplePlayerDTO4, simplePlayerDTO5));

        final List<PlayerDTO> sortedPlayerDTO3 = SortUtil.sortPreflop(this.playersForTestingPreflop);

        Assertions.assertEquals(sortedPlayerDTO3.get(0).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayerDTO3.get(1).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayerDTO3.get(2).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayerDTO3.get(3).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayerDTO3.get(4).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayerDTO3.get(5).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayerDTO3.get(6).getRoleType(), RoleType.BUTTON);
        Assertions.assertEquals(sortedPlayerDTO3.get(7).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayerDTO3.get(8).getRoleType(), RoleType.BIG_BLIND);

    }


    @Test
    public void testSortPostFlop() {
        final List<PlayerDTO> playerDTOS = SortUtil.sortPostflop(playerDTOForTestingPostFlop);

        Assertions.assertEquals(playerDTOS.get(0).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(playerDTOS.get(1).getRoleType(), RoleType.BIG_BLIND);
        Assertions.assertEquals(playerDTOS.get(2).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(playerDTOS.get(3).getRoleType(), RoleType.BUTTON);

        final PlayerDTO simplePlayerDTO1 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayerDTO1.setRole(RoleType.PLAYER);

        final PlayerDTO simplePlayerDTO2 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayerDTO1.setRole(RoleType.PLAYER);


        playerDTOForTestingPostFlop.addAll(Arrays.asList(simplePlayerDTO1, simplePlayerDTO2));

        Collections.shuffle(playerDTOForTestingPostFlop);

        final List<PlayerDTO> sortedPlayersWithShuffle = SortUtil.sortPostflop(playerDTOForTestingPostFlop);


        Assertions.assertEquals(sortedPlayersWithShuffle.get(0).getRoleType(), RoleType.SMALL_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(1).getRoleType(), RoleType.BIG_BLIND);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(2).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(3).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(4).getRoleType(), RoleType.PLAYER);
        Assertions.assertEquals(sortedPlayersWithShuffle.get(5).getRoleType(), RoleType.BUTTON);


        final PlayerDTO simplePlayerDTO3 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayerDTO1.setRole(RoleType.PLAYER);

        final PlayerDTO simplePlayerDTO4 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayerDTO1.setRole(RoleType.PLAYER);

        final PlayerDTO simplePlayerDTO5 = new PlayerDTO(faker.name().name(), 5000);
        simplePlayerDTO1.setRole(RoleType.PLAYER);

        playerDTOForTestingPostFlop.addAll(Arrays.asList(simplePlayerDTO3, simplePlayerDTO4, simplePlayerDTO5));

        Collections.shuffle(playerDTOForTestingPostFlop);

        final List<PlayerDTO> sortedPlayersWithShuffle2 = SortUtil.sortPostflop(playerDTOForTestingPostFlop);


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

            final List<PlayerDTO> playerDTOS = SortUtil.sortPreflop(playersForTestingPreflop);

            Assertions.assertEquals(playerDTOS.get(0).getRoleType(), RoleType.PLAYER);
            Assertions.assertEquals(playerDTOS.get(1).getRoleType(), RoleType.BUTTON);
            Assertions.assertEquals(playerDTOS.get(2).getRoleType(), RoleType.SMALL_BLIND);
            Assertions.assertEquals(playerDTOS.get(3).getRoleType(), RoleType.BIG_BLIND);

        }
    }

    @Test
    public void loadTestShufflingPostFlop(){

        for (int i = 0; i < 1_000_000; i++) {
            Collections.shuffle(playerDTOForTestingPostFlop);

            final List<PlayerDTO> playerDTOS = SortUtil.sortPostflop(playerDTOForTestingPostFlop);

            Assertions.assertEquals(playerDTOS.get(0).getRoleType(), RoleType.SMALL_BLIND);
            Assertions.assertEquals(playerDTOS.get(1).getRoleType(), RoleType.BIG_BLIND);
            Assertions.assertEquals(playerDTOS.get(2).getRoleType(), RoleType.PLAYER);
            Assertions.assertEquals(playerDTOS.get(3).getRoleType(), RoleType.BUTTON);

        }
    }
}
