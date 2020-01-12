package ru.sm.poker.util;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.model.Player;

import java.util.Collections;

public class DTOUtilTest {
    private final static String DEFAULT_GAME_NAME = "test";
    public final static long DEFAULT_CHIPS_COUNT = 5000L;
    private final static long DEFAULT_BIG_BLIND_BET =  2L;
    private final static long DEFAULT_SMALL_BLIND_BET = 1L;

    public static RoundSettingsDTO getRoundSettingsDTO(long lastBet, StageType stageType) {
        return RoundSettingsDTO.builder()
                .lastBet(lastBet)
                .stageType(stageType)
                .bigBlindBet(DEFAULT_BIG_BLIND_BET)
                .smallBlindBet(DEFAULT_SMALL_BLIND_BET)
                .players(Collections.emptyList())
                .build();
    }

    public static RoundSettingsDTO getRoundSettingsDTO(long lastBet) {
        return RoundSettingsDTO.builder()
                .lastBet(lastBet)
                .players(Collections.emptyList())
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
