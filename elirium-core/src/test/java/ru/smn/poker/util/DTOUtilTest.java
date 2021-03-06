package ru.smn.poker.util;

import com.github.javafaker.Faker;
import ru.smn.combination.data.CardType;
import ru.smn.poker.dto.ClassicTableSettings;
import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.CardEntity;
import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.PlayerSettingsEntity;
import ru.smn.poker.enums.RoleType;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.enums.StateType;
import ru.smn.poker.game.TableSettings;

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

    public static TableSettings getRoundSettingsDTO() {
        return getRoundSettingsDTO(DEFAULT_LAST_BET, Collections.emptyList());
    }

    public static TableSettings getRoundSettingsDTOWithPlayers(int count) {
        return getRoundSettingsDTO(DEFAULT_LAST_BET, getPlayers(count));
    }

    public static TableSettings getRoundSettingsDTO(StageType stageType) {
        return getRoundSettingsDTO(DEFAULT_PLAYERS_SIZE, stageType);
    }

    public static TableSettings getRoundSettingsDTO(int countPlayers, StageType stageType) {
        return getRoundSettingsDTO(DEFAULT_LAST_BET, stageType, getPlayers(countPlayers));
    }

    public static TableSettings getRoundSettingsDTO(long lastBet, StageType stageType, List<PlayerEntity> players) {
        return ClassicTableSettings.builder()
                .lastBet(lastBet)
                .stageType(stageType)
                .bigBlindBet(DEFAULT_BIG_BLIND_BET)
                .smallBlindBet(DEFAULT_SMALL_BLIND_BET)
                .bank(DEFAULT_BIG_BLIND_BET + DEFAULT_SMALL_BLIND_BET)
                .players(players)
                .stageHistory(new HashMap<>())
                .build();
    }

    public static TableSettings getRoundSettingsDTO(long lastBet) {
        return getRoundSettingsDTO(lastBet, Collections.emptyList());
    }

    public static TableSettings getRoundSettingsDTO(long lastBet, List<PlayerEntity> players) {
        return ClassicTableSettings.builder()
                .lastBet(lastBet)
                .bigBlindBet(DEFAULT_BIG_BLIND_BET)
                .smallBlindBet(DEFAULT_SMALL_BLIND_BET)
                .fullHistory(new HashMap<>())
                .bank(DEFAULT_BIG_BLIND_BET + DEFAULT_SMALL_BLIND_BET)
                .stageHistory(new HashMap<>())
                .players(players)
                .build();
    }

    public static List<PlayerEntity> getPlayers(int count) {
        return IntStream.range(0, count).mapToObj(i -> getPlayer()).collect(Collectors.toList());
    }

    public static PlayerEntity getPlayer() {
        return PlayerEntity.builder()
                .name(faker.name().name())
                .settings(Collections.singletonList(PlayerSettingsEntity.builder()
                        .tableName(DEFAULT_GAME_NAME)
                        .chipsCount(new ChipsCountEntity(5000L))
                        .stateType(StateType.IN_GAME)
                        .cards(List.of(
                                CardEntity.builder().cardType(CardType.SIX_S).build(),
                                CardEntity.builder().cardType(CardType.SIX_C).build())
                        )
                        .roleType(RoleType.ORDINARY)
                        .timeBank(60L)
                        .build()))
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
