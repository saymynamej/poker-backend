package ru.smn.poker.util;

import com.github.javafaker.Faker;
import ru.smn.poker.action.holdem.Wait;
import ru.smn.poker.dto.Card;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.enums.RoleType;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.enums.StateType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DTOUtilTest {

    public final static Faker faker = new Faker();
    public final static String DEFAULT_GAME_NAME = "test";
    public final static long DEFAULT_CHIPS_COUNT = 5000L;
    public final static long DEFAULT_BIG_BLIND_BET = 2L;
    public final static long DEFAULT_SMALL_BLIND_BET = 1L;
    public final static long DEFAULT_LAST_BET = DEFAULT_BIG_BLIND_BET;
    public final static int DEFAULT_PLAYERS_SIZE = 9;

    public static RoundSettings getRoundSettingsDTO() {
        return getRoundSettingsDTO(DEFAULT_LAST_BET, Collections.emptyList());
    }

    public static RoundSettings getRoundSettingsDTOWithPlayers(int count) {
        return getRoundSettingsDTO(DEFAULT_LAST_BET, getPlayers(count));
    }

    public static RoundSettings getRoundSettingsDTO(StageType stageType) {
        return getRoundSettingsDTO(DEFAULT_PLAYERS_SIZE, stageType);
    }

    public static RoundSettings getRoundSettingsDTO(int countPlayers, StageType stageType) {
        return getRoundSettingsDTO(DEFAULT_LAST_BET, stageType, getPlayers(countPlayers));
    }

    public static RoundSettings getRoundSettingsDTO(long lastBet, StageType stageType, List<Player> players) {
        return HoldemRoundSettings.builder()
                .lastBet(lastBet)
                .stageType(stageType)
                .bigBlindBet(DEFAULT_BIG_BLIND_BET)
                .smallBlindBet(DEFAULT_SMALL_BLIND_BET)
                .bank(DEFAULT_BIG_BLIND_BET + DEFAULT_SMALL_BLIND_BET)
                .players(players)
                .stageHistory(new HashMap<>())
                .build();
    }

    public static RoundSettings getRoundSettingsDTO(long lastBet) {
        return getRoundSettingsDTO(lastBet, Collections.emptyList());
    }

    public static RoundSettings getRoundSettingsDTO(long lastBet, List<Player> players) {
        return HoldemRoundSettings.builder()
                .lastBet(lastBet)
                .bigBlindBet(DEFAULT_BIG_BLIND_BET)
                .smallBlindBet(DEFAULT_SMALL_BLIND_BET)
                .fullHistory(new HashMap<>())
                .bank(DEFAULT_BIG_BLIND_BET + DEFAULT_SMALL_BLIND_BET)
                .stageHistory(new HashMap<>())
                .players(players)
                .build();
    }

    public static List<Player> getPlayers(int count) {
        return IntStream.range(0, count).mapToObj(i -> getPlayer()).collect(Collectors.toList());
    }

    public static Player getPlayer() {
        return Player.builder()
                .gameName(DEFAULT_GAME_NAME)
                .chipsCount(DEFAULT_CHIPS_COUNT)
                .name(faker.name().name())
                .stateType(StateType.IN_GAME)
                .cards(List.of(
                        Card.builder().cardType(CardType.SIX_S).build(),
                        Card.builder().cardType(CardType.SIX_C).build())
                )
                .roleType(RoleType.ORDINARY)
                .action(new Wait())
                .timeBank(60L)
                .build();
    }

    public static Player getPlayer(RoleType roleType) {
        return Player.builder()
                .gameName(DEFAULT_GAME_NAME)
                .chipsCount(DEFAULT_CHIPS_COUNT)
                .roleType(roleType)
                .build();
    }
}
