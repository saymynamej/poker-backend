package ru.smn.poker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.HoldemRoundSettingsManager;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.game.RoundSettingsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ru.smn.poker.util.DTOUtilTest.*;

class HoldemRoundSettingsManagerTest {

    @Test
    void testCommonSettingsPreflop() {
        final List<PlayerEntity> players = getPlayers();

        final RoundSettingsManager roundSettingsManager = getRoundSettingsManager(players);

        final RoundSettings preflopSettings = roundSettingsManager.getSettings(null);

        Assertions.assertEquals(
                preflopSettings.getBigBlindBet() + preflopSettings.getSmallBlindBet(),
                preflopSettings.getBank()
        );
        Assertions.assertEquals(
                preflopSettings.getStageType(), StageType.PREFLOP
        );
        Assertions.assertEquals(
                preflopSettings.getLastBet(), preflopSettings.getBigBlindBet()
        );
        Assertions.assertTrue(
                preflopSettings.getPlayers().stream()
                        .noneMatch(player -> player.getRoleType() == null)
        );

        Assertions.assertTrue(
                preflopSettings.getPlayers().stream().anyMatch(PlayerEntity::isBigBlind)
        );
        Assertions.assertTrue(
                preflopSettings.getPlayers().stream().anyMatch(PlayerEntity::isSmallBlind)
        );
        Assertions.assertTrue(
                preflopSettings.getPlayers().stream().anyMatch(PlayerEntity::isButton)
        );
        Assertions.assertTrue(
                preflopSettings.getPlayers().stream().anyMatch(PlayerEntity::isPlayer)
        );

        Assertions.assertNull(preflopSettings.getFlop());
        Assertions.assertNull(preflopSettings.getTern());
        Assertions.assertNull(preflopSettings.getRiver());
    }

    @Test
    void testOrderPreflopSettings() {
        final List<PlayerEntity> players = getPlayers();

        for (int i = 0; i < 10_000; i++) {
            final RoundSettingsManager firstRound = getRoundSettingsManager(players);

            final RoundSettings preflopSettings = firstRound.getSettings(null);

            final RoundSettingsManager secondRound = getRoundSettingsManager(players);

            final RoundSettings preflopSettings2 = secondRound.getSettings(null);

            final int firstButtonIndex = preflopSettings.getPlayers().indexOf(preflopSettings.getButton());
            final int secondButtonIndex = preflopSettings2.getPlayers().indexOf(preflopSettings2.getButton());

            final int firstSmallBlindIndex = preflopSettings.getPlayers().indexOf(preflopSettings.getSmallBlind());
            final int secondSmallBlindIndex = preflopSettings2.getPlayers().indexOf(preflopSettings2.getSmallBlind());

            final int firstBigBlindIndex = preflopSettings.getPlayers().indexOf(preflopSettings.getBigBlind());
            final int secondBigBlindIndex = preflopSettings2.getPlayers().indexOf(preflopSettings2.getBigBlind());

            final int expectedButtonValue = players.size() % (secondButtonIndex - firstButtonIndex) == 0 ? 0 : 1;
            final int expectedSmallBlindValue = players.size() % (secondSmallBlindIndex - firstSmallBlindIndex) == 0 ? 0 : 1;
            final int expectedBigBlindValue = players.size() % (secondBigBlindIndex - firstBigBlindIndex) == 0 ? 0 : 1;

            Assertions.assertEquals(expectedButtonValue, players.size() % (secondButtonIndex - firstButtonIndex));
            Assertions.assertEquals(expectedSmallBlindValue, players.size() % (secondSmallBlindIndex - firstSmallBlindIndex));
            Assertions.assertEquals(expectedBigBlindValue, players.size() % (secondBigBlindIndex - firstBigBlindIndex));
        }

    }


    @Test
    void testPostFlopSettings() {
        final List<PlayerEntity> players = getPlayers();

        final RoundSettingsManager firstRound = getRoundSettingsManager(players);

        final RoundSettings preflopSettings = firstRound.getSettings(null);

        final RoundSettings postFlopSettings = firstRound.getSettings(preflopSettings);

        Assertions.assertEquals(
                preflopSettings.getBank(),
                postFlopSettings.getBank()
        );
        Assertions.assertEquals(
                postFlopSettings.getStageType(), StageType.FLOP
        );
        Assertions.assertEquals(
                0, postFlopSettings.getLastBet()
        );
        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream()
                        .noneMatch(player -> player.getRoleType() == null)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(PlayerEntity::isBigBlind)
        );
        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(PlayerEntity::isSmallBlind)
        );
        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(PlayerEntity::isButton)
        );
        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(PlayerEntity::isPlayer)
        );


        Assertions.assertEquals(preflopSettings.getButton(), postFlopSettings.getButton());
        Assertions.assertEquals(preflopSettings.getSmallBlind(), postFlopSettings.getSmallBlind());
        Assertions.assertEquals(preflopSettings.getBigBlind(), postFlopSettings.getBigBlind());

        Assertions.assertNotNull(postFlopSettings.getFlop());
        Assertions.assertEquals(3, postFlopSettings.getFlop().size());
        Assertions.assertNull(postFlopSettings.getTern());
        Assertions.assertNull(postFlopSettings.getRiver());

    }


    @Test
    void testPostFlopSettingsWithTern() {
        final List<PlayerEntity> players = getPlayers();

        final RoundSettingsManager firstRound = getRoundSettingsManager(players);

        final RoundSettings preflopSettings = firstRound.getSettings(null);

        final RoundSettings postFlopSettings = firstRound.getSettings(preflopSettings);

        final RoundSettings postFlopSettingsWithTern = firstRound.getSettings(postFlopSettings);

        Assertions.assertEquals(
                postFlopSettings.getBank(),
                postFlopSettingsWithTern.getBank()
        );

        Assertions.assertEquals(
                postFlopSettings.getStageType(), StageType.FLOP
        );

        Assertions.assertEquals(
                0, postFlopSettings.getLastBet()
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream()
                        .noneMatch(player -> player.getRoleType() == null)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(PlayerEntity::isBigBlind)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(PlayerEntity::isSmallBlind)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(PlayerEntity::isButton)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(PlayerEntity::isPlayer)
        );

        Assertions.assertEquals(preflopSettings.getButton(), postFlopSettings.getButton());
        Assertions.assertEquals(postFlopSettings.getButton(), postFlopSettingsWithTern.getButton());

        Assertions.assertEquals(preflopSettings.getSmallBlind(), postFlopSettings.getSmallBlind());
        Assertions.assertEquals(postFlopSettings.getSmallBlind(), postFlopSettingsWithTern.getSmallBlind());

        Assertions.assertEquals(preflopSettings.getBigBlind(), postFlopSettings.getBigBlind());
        Assertions.assertEquals(postFlopSettings.getBigBlind(), postFlopSettingsWithTern.getBigBlind());


        Assertions.assertNotNull(postFlopSettingsWithTern.getFlop());
        Assertions.assertNotNull(postFlopSettingsWithTern.getTern());
        Assertions.assertNull(postFlopSettingsWithTern.getRiver());

    }


    @Test
    void testPostFlopSettingsWithRiver() {
        final List<PlayerEntity> players = getPlayers();

        final RoundSettingsManager firstRound = getRoundSettingsManager(players);

        final RoundSettings preflopSettings = firstRound.getSettings(null);

        final RoundSettings postFlopSettings = firstRound.getSettings(preflopSettings);

        final RoundSettings postFlopSettingsWithTern = firstRound.getSettings(postFlopSettings);

        final RoundSettings postFlopSettingsWithRiver = firstRound.getSettings(postFlopSettingsWithTern);


        Assertions.assertEquals(
                postFlopSettingsWithTern.getBank(),
                postFlopSettingsWithRiver.getBank()
        );

        Assertions.assertEquals(
                postFlopSettings.getStageType(), StageType.FLOP
        );

        Assertions.assertEquals(
                0, postFlopSettings.getLastBet()
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream()
                        .noneMatch(player -> player.getRoleType() == null)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(PlayerEntity::isBigBlind)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(PlayerEntity::isSmallBlind)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(PlayerEntity::isButton)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(PlayerEntity::isPlayer)
        );


        Assertions.assertEquals(preflopSettings.getButton(), postFlopSettings.getButton());
        Assertions.assertEquals(postFlopSettings.getButton(), postFlopSettingsWithTern.getButton());
        Assertions.assertEquals(postFlopSettingsWithTern.getButton(), postFlopSettingsWithRiver.getButton());

        Assertions.assertEquals(preflopSettings.getSmallBlind(), postFlopSettings.getSmallBlind());
        Assertions.assertEquals(postFlopSettings.getSmallBlind(), postFlopSettingsWithTern.getSmallBlind());
        Assertions.assertEquals(postFlopSettingsWithTern.getSmallBlind(), postFlopSettingsWithRiver.getSmallBlind());

        Assertions.assertEquals(preflopSettings.getBigBlind(), postFlopSettings.getBigBlind());
        Assertions.assertEquals(postFlopSettings.getBigBlind(), postFlopSettingsWithTern.getBigBlind());
        Assertions.assertEquals(postFlopSettingsWithTern.getBigBlind(), postFlopSettingsWithRiver.getBigBlind());

        Assertions.assertNotNull(postFlopSettingsWithRiver.getFlop());
        Assertions.assertNotNull(postFlopSettingsWithRiver.getTern());
        Assertions.assertNotNull(postFlopSettingsWithRiver.getRiver());

    }

    private RoundSettingsManager getRoundSettingsManager(List<PlayerEntity> players) {
        return new HoldemRoundSettingsManager(
                new Random(),
                players,
                DEFAULT_GAME_NAME,
                DEFAULT_SMALL_BLIND_BET,
                DEFAULT_BIG_BLIND_BET,
                0L
        );
    }

    public List<PlayerEntity> getPlayers() {
        final List<PlayerEntity> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(getPlayer());
        }
        return players;
    }

}
