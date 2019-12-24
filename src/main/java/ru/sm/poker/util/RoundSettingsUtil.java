package ru.sm.poker.util;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;

import java.util.List;

public class RoundSettingsUtil {

    public static RoundSettingsDTO copyWithSecureCard(RoundSettingsDTO roundSettingsDTO, List<Player> players) {
        return RoundSettingsDTO.builder()
                .gameName(roundSettingsDTO.getGameName())
                .bank(roundSettingsDTO.getBank())
                .smallBlindBet(roundSettingsDTO.getSmallBlindBet())
                .bigBlindBet(roundSettingsDTO.getBigBlindBet())
                .bigBlind(roundSettingsDTO.getBigBlind())
                .smallBlind(roundSettingsDTO.getSmallBlind())
                .button(roundSettingsDTO.getButton())
                .players(players)
                .stageType(roundSettingsDTO.getStageType())
                .lastBet(roundSettingsDTO.getLastBet())
                .activePlayer(roundSettingsDTO.getActivePlayer())
                .flop(roundSettingsDTO.getFlop())
                .river(roundSettingsDTO.getRiver())
                .tern(roundSettingsDTO.getTern())
                .build();
    }

}
