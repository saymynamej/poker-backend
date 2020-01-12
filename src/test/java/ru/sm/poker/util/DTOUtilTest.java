package ru.sm.poker.util;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.model.Player;

import java.util.Collections;

public class DTOUtilTest {

    public static RoundSettingsDTO getRoundSettingsDTO(long bet) {
        return RoundSettingsDTO.builder()
                .lastBet(bet)
                .players(Collections.emptyList())
                .build();
    }


    public static Player getPlayer(String gameName, long chipsCount) {
        return Player.builder()
                .gameName(gameName)
                .chipsCount(chipsCount)
                .roleType(RoleType.PLAYER)
                .build();
    }
}
