package ru.sm.poker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.game.RoundSettingsManager;
import ru.sm.poker.game.holdem.HoldemRoundSettingsManager;
import ru.sm.poker.model.Player;

import java.util.ArrayList;
import java.util.List;

import static ru.sm.poker.util.DTOUtilTest.*;

class HoldemRoundSettingsManagerTest {

    @Test
    void testCommonSettingsPreflop() {
        final List<Player> players = getPlayers();

        final RoundSettingsManager roundSettingsManager = getRoundSettingsManager(players);

        final HoldemRoundSettingsDTO preflopSettings = roundSettingsManager.getPreflopSettings();
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
                preflopSettings.getPlayers().stream().anyMatch(Player::isBigBlind)
        );
        Assertions.assertTrue(
                preflopSettings.getPlayers().stream().anyMatch(Player::isSmallBlind)
        );
        Assertions.assertTrue(
                preflopSettings.getPlayers().stream().anyMatch(Player::isButton)
        );
        Assertions.assertTrue(
                preflopSettings.getPlayers().stream().anyMatch(Player::isPlayer)
        );

        Assertions.assertNull(preflopSettings.getFlop());
        Assertions.assertNull(preflopSettings.getTern());
        Assertions.assertNull(preflopSettings.getRiver());
    }

    @Test
    void testOrderPreflopSettings() {
        final List<Player> players = getPlayers();

        for (int i = 0; i < 10_000; i++) {
            final RoundSettingsManager firstRound = getRoundSettingsManager(players);

            final HoldemRoundSettingsDTO preflopSettings = firstRound.getPreflopSettings();

            final RoundSettingsManager secondRound = getRoundSettingsManager(players);

            final HoldemRoundSettingsDTO preflopSettings2 = secondRound.getPreflopSettings();

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
        final List<Player> players = getPlayers();

        final RoundSettingsManager firstRound = getRoundSettingsManager(players);

        final HoldemRoundSettingsDTO preflopSettings = firstRound.getPreflopSettings();

        final HoldemRoundSettingsDTO postFlopSettings = firstRound.getPostFlopSettings(preflopSettings.getBank());

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
                postFlopSettings.getPlayers().stream().anyMatch(Player::isBigBlind)
        );
        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(Player::isSmallBlind)
        );
        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(Player::isButton)
        );
        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(Player::isPlayer)
        );


        Assertions.assertEquals(preflopSettings.getButton(), postFlopSettings.getButton());
        Assertions.assertEquals(preflopSettings.getSmallBlind(), postFlopSettings.getSmallBlind());
        Assertions.assertEquals(preflopSettings.getBigBlind(), postFlopSettings.getBigBlind());

        Assertions.assertNotNull(postFlopSettings.getFlop());
        Assertions.assertNull(postFlopSettings.getTern());
        Assertions.assertNull(postFlopSettings.getRiver());

    }


    @Test
    void testPostFlopSettingsWithTern() {
        final List<Player> players = getPlayers();

        final RoundSettingsManager firstRound = getRoundSettingsManager(players);

        final HoldemRoundSettingsDTO preflopSettings = firstRound.getPreflopSettings();

        final HoldemRoundSettingsDTO postFlopSettings = firstRound.getPostFlopSettings(preflopSettings.getBank());

        final HoldemRoundSettingsDTO postFlopSettingsWithTern = firstRound.getPostFlopSettingsWithTern(postFlopSettings.getBank());

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
                postFlopSettings.getPlayers().stream().anyMatch(Player::isBigBlind)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(Player::isSmallBlind)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(Player::isButton)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(Player::isPlayer)
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
        final List<Player> players = getPlayers();

        final RoundSettingsManager firstRound = getRoundSettingsManager(players);

        final HoldemRoundSettingsDTO preflopSettings = firstRound.getPreflopSettings();

        final HoldemRoundSettingsDTO postFlopSettings = firstRound.getPostFlopSettings(preflopSettings.getBank());

        final HoldemRoundSettingsDTO postFlopSettingsWithTern = firstRound.getPostFlopSettingsWithTern(postFlopSettings.getBank());

        final HoldemRoundSettingsDTO postFlopSettingsWithRiver = firstRound.getPostFlopSettingsWithRiver(postFlopSettingsWithTern.getBank());


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
                postFlopSettings.getPlayers().stream().anyMatch(Player::isBigBlind)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(Player::isSmallBlind)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(Player::isButton)
        );

        Assertions.assertTrue(
                postFlopSettings.getPlayers().stream().anyMatch(Player::isPlayer)
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

    private RoundSettingsManager getRoundSettingsManager(List<Player> players) {
        return new HoldemRoundSettingsManager(
                players,
                DEFAULT_GAME_NAME,
                DEFAULT_SMALL_BLIND_BET,
                DEFAULT_BIG_BLIND_BET
        );
    }

    public List<Player> getPlayers() {
        final List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(getPlayer());
        }
        return players;
    }

}
