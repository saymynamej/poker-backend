package ru.sm.poker.util;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;

import java.util.Collections;
import java.util.List;

public class RoundSettingsUtil {

    public static HoldemRoundSettingsDTO copyWithSecureCard(HoldemRoundSettingsDTO holdemRoundSettings, List<String> filters) {
        final List<PlayerDTO> playersWithSecureCards = PlayerUtil.copies(holdemRoundSettings.getPlayers());
        playersWithSecureCards.forEach(player -> {
            if (!filters.contains(player.getName())) {
                player.addCards(Collections.emptyList());
            }
        });

        return copy(holdemRoundSettings, playersWithSecureCards);
    }

    public static HoldemRoundSettingsDTO copy(HoldemRoundSettingsDTO holdemRoundSettings, List<PlayerDTO> players) {
        return HoldemRoundSettingsDTO.builder()
                .gameName(holdemRoundSettings.getGameName())
                .bank(holdemRoundSettings.getBank())
                .smallBlindBet(holdemRoundSettings.getSmallBlindBet())
                .bigBlindBet(holdemRoundSettings.getBigBlindBet())
                .bigBlind(holdemRoundSettings.getBigBlind())
                .smallBlind(holdemRoundSettings.getSmallBlind())
                .button(holdemRoundSettings.getButton())
                .players(players)
                .stageType(holdemRoundSettings.getStageType())
                .lastBet(holdemRoundSettings.getLastBet())
                .activePlayer(holdemRoundSettings.getActivePlayer())
                .flop(holdemRoundSettings.getFlop())
                .river(holdemRoundSettings.getRiver())
                .tern(holdemRoundSettings.getTern())
                .build();
    }

}
