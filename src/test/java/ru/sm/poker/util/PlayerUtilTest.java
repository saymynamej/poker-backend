package ru.sm.poker.util;

import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.api.Assertions;
import org.springframework.security.core.parameters.P;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.enums.StateType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerUtilTest {

    private final List<PlayerDTO> playerExcepted = new ArrayList<>();

    @Before
    public void before() {
        Wait wait = new Wait();
        PlayerDTO simplePlayer1 = new PlayerDTO("John", Collections.singletonList(CardType.EIGHT_S), RoleType.PLAYER, 200L, StateType.IN_GAME, 60L, wait, true, "test");
        PlayerDTO simplePlayer2 = new PlayerDTO("Paul", Collections.singletonList(CardType.A_D), RoleType.BUTTON, 200L, StateType.IN_GAME, 60L, wait, true, "test");
        PlayerDTO simplePlayer3 = new PlayerDTO("Bill", Collections.singletonList(CardType.EIGHT_C), RoleType.SMALL_BLIND, 200L, StateType.IN_GAME, 60L, wait, true, "test");
        PlayerDTO simplePlayer4 = new PlayerDTO("Bob", Collections.singletonList(CardType.FIVE_D), RoleType.BIG_BLIND, 200L, StateType.IN_GAME, 60L, wait, true, "test");
        PlayerDTO simplePlayer5 = new PlayerDTO("Rick", Collections.singletonList(CardType.FOUR_S), RoleType.PLAYER, 200L, StateType.AFK, 60L, wait, true, "test");

        playerExcepted.addAll(Arrays.asList(simplePlayer2,simplePlayer3,simplePlayer1,simplePlayer4,simplePlayer5));

    }


    @Test
    public void getPlayersInGameTest() {
        List<PlayerDTO> playersInGame = PlayerUtil.getPlayersInGame(playerExcepted);
        Assertions.assertEquals(4,playersInGame.size());
        boolean isNickExist = playersInGame.stream()
                .noneMatch(playerDTO -> playerDTO.getName().equals("Nick"));
        Assertions.assertTrue(isNickExist);

    }
    @Test
    public void copiesTest(){
        List<PlayerDTO> copies = PlayerUtil.copies(playerExcepted);
        Assertions.assertEquals(playerExcepted.size(),copies.size());
        for(PlayerDTO playerDTO :playerExcepted){
            boolean contains = copies.contains(playerDTO);
            Assertions.assertTrue(contains);
        }
    }

}
