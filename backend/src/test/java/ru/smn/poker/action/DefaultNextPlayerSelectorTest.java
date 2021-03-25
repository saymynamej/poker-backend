package ru.smn.poker.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.smn.poker.action.holdem.Check;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StateType;
import ru.smn.poker.util.PlayerUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class DefaultNextPlayerSelectorTest {

    @Autowired
    private DefaultNextPlayerSelector defaultNextPlayerSelector;

    @Test
    void testGetPlayerForAction() {
        final PlayerEntity defaultPlayerForHoldem1 = PlayerUtil.getDefaultPlayerForHoldem("1");
        final PlayerEntity defaultPlayerForHoldem2 = PlayerUtil.getDefaultPlayerForHoldem("2");
        final PlayerEntity defaultPlayerForHoldem3 = PlayerUtil.getDefaultPlayerForHoldem("3");
        final PlayerEntity defaultPlayerForHoldem4 = PlayerUtil.getDefaultPlayerForHoldem("4");

        final List<PlayerEntity> defaultPlayersForHoldem = List.of(
                defaultPlayerForHoldem1,
                defaultPlayerForHoldem2,
                defaultPlayerForHoldem3,
                defaultPlayerForHoldem4
        );


        defaultPlayersForHoldem.forEach(playerEntity -> {
            playerEntity.getTableSettings().setStateType(StateType.IN_GAME);
            playerEntity.getTableSettings().setAction(new Check());
        });

        final PlayerEntity firstPlayerForAction = defaultNextPlayerSelector.getPlayerForAction(defaultPlayersForHoldem, null);
        Assertions.assertEquals(defaultPlayerForHoldem1, firstPlayerForAction);

        final PlayerEntity secondPlayerForAction = defaultNextPlayerSelector.getPlayerForAction(defaultPlayersForHoldem, defaultPlayerForHoldem1);
        Assertions.assertEquals(defaultPlayerForHoldem2, secondPlayerForAction);

        final PlayerEntity thirdPlayerForAction = defaultNextPlayerSelector.getPlayerForAction(defaultPlayersForHoldem, defaultPlayerForHoldem2);
        Assertions.assertEquals(defaultPlayerForHoldem3, thirdPlayerForAction);

        final PlayerEntity fourthPlayerForAction = defaultNextPlayerSelector.getPlayerForAction(defaultPlayersForHoldem, defaultPlayerForHoldem3);
        Assertions.assertEquals(defaultPlayerForHoldem4, fourthPlayerForAction);

        final PlayerEntity fifthPlayerForAction = defaultNextPlayerSelector.getPlayerForAction(defaultPlayersForHoldem, defaultPlayerForHoldem4);
        Assertions.assertEquals(defaultPlayerForHoldem1, fifthPlayerForAction);
    }
}
