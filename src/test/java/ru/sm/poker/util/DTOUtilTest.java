package ru.sm.poker.util;

import com.github.javafaker.Faker;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.enums.StateType;

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


    public static HoldemRoundSettings getRoundSettingsDTO() {
        return getRoundSettingsDTO(DEFAULT_LAST_BET, Collections.emptyList());
    }

    public static HoldemRoundSettings getRoundSettingsDTOWithPlayers(int count) {
        return getRoundSettingsDTO(DEFAULT_LAST_BET, getPlayers(count));
    }

    public static HoldemRoundSettings getRoundSettingsDTO(StageType stageType) {
        return getRoundSettingsDTO(DEFAULT_PLAYERS_SIZE, stageType);
    }

    public static HoldemRoundSettings getRoundSettingsDTO(int countPlayers, StageType stageType) {
        return getRoundSettingsDTO(DEFAULT_LAST_BET, stageType, getPlayers(countPlayers));
    }

    public static HoldemRoundSettings getRoundSettingsDTO(long lastBet, StageType stageType, List<Player> players) {
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

    public static HoldemRoundSettings getRoundSettingsDTO(long lastBet) {
        return getRoundSettingsDTO(lastBet, Collections.emptyList());
    }

    public static HoldemRoundSettings getRoundSettingsDTO(long lastBet, List<Player> players) {
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
                .cards(List.of(CardType.SIX_S, CardType.SIX_C))
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
