package ru.sm.poker.util;

import com.github.javafaker.Faker;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.dto.PlayerDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
                .playerDTOS(Collections.emptyList())
                .build();
    }

    public static HoldemRoundSettingsDTO getRoundSettingsDTO(long lastBet) {
        return HoldemRoundSettingsDTO.builder()
                .lastBet(lastBet)
                .playerDTOS(Collections.emptyList())
                .build();
    }


    public List<PlayerDTO> getPlayers(int count) {
        return IntStream.of(0, count).mapToObj(i -> getPlayer())
                .collect(Collectors.toList());
    }

    public static PlayerDTO getPlayer() {
        return PlayerDTO.builder()
                .gameName(DEFAULT_GAME_NAME)
                .chipsCount(DEFAULT_CHIPS_COUNT)
                .name(faker.name().name())
                .timeBank(60L)
                .build();
    }

    public static PlayerDTO getPlayer(RoleType roleType) {
        return PlayerDTO.builder()
                .gameName(DEFAULT_GAME_NAME)
                .chipsCount(DEFAULT_CHIPS_COUNT)
                .roleType(roleType)
                .build();
    }
}
