package ru.sm.poker.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerUtilTest  {

    private final List<Player> excepted = new ArrayList<>();

    @Before
    public void before (){
        final Player testOne = new Player("Ivan", 500);
        testOne.setRole(RoleType.PLAYER);
        testOne.setActive(true);
        testOne.setStateType(StateType.IN_GAME);
        final Player testTwo = new Player("Oleg",500);
        testTwo.setRole(RoleType.BIG_BLIND);
        testTwo.setActive(true);
        testTwo.setStateType(StateType.IN_GAME);
        final Player testThree = new Player("Bill",200);
        testThree.setRole(RoleType.PLAYER);
        testThree.setActive(true);
        testThree.setStateType(StateType.IN_GAME);
        final Player testFour = new Player("Dick",300);
        testFour.setStateType(null);
        testFour.setActive(false);
        final Player testFive = new Player("Edie",400);
        testFive.setRole(RoleType.BUTTON);
        testFive.setActive(true);
        testFive.setStateType(StateType.IN_GAME);
        excepted.addAll(Arrays.asList(testOne,testTwo,testThree,testFive));

    }


    @Test
    public void getPlayersInGame (){
        for (Player player : excepted) {
            Assertions.assertNotSame(player.getStateType(), StateType.AFK);
            Assertions.assertNotNull(player.getRoleType());
        }
    }

}
