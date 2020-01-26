package ru.sm.poker.util;

import com.github.javafaker.Faker;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;

import java.util.Collections;

public class DTOUtilTest {

    public final static Faker faker = new Faker();
    public final static String DEFAULT_GAME_NAME = "test";
    public final static long DEFAULT_CHIPS_COUNT = 5000L;
    public final static long DEFAULT_BIG_BLIND_BET = 2L;
    public final static long DEFAULT_SMALL_BLIND_BET = 1L;

    public static HoldemRoundSettingsDTO getRoundSettingsDTO(long lastBet, StageType stageType) {
        return HoldemRoundSettingsDTO.builder()
                .lastBet(lastBet)
                .stageType(stageType)
                .bigBlindBet(DEFAULT_BIG_BLIND_BET)
                .smallBlindBet(DEFAULT_SMALL_BLIND_BET)
                .players(Collections.emptyList())
                .build();
    }

    public static HoldemRoundSettingsDTO getRoundSettingsDTO(long lastBet) {
        return HoldemRoundSettingsDTO.builder()
                .lastBet(lastBet)
                .players(Collections.emptyList())
                .build();
    }

    public static Player getPlayer() {
        return Player.builder()
                .gameName(DEFAULT_GAME_NAME)
                .chipsCount(DEFAULT_CHIPS_COUNT)
                .name(faker.name().name())
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
