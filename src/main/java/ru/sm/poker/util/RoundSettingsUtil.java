package ru.sm.poker.util;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.Player;

import java.util.Collections;
import java.util.List;

public class RoundSettingsUtil {

    public static HoldemRoundSettingsDTO copyWithSecureCard(HoldemRoundSettingsDTO holdemRoundSettingsDTO, List<String> filters) {
        final List<Player> playersWithSecureCards = PlayerUtil.copies(holdemRoundSettingsDTO.getPlayers());
        playersWithSecureCards.forEach(player -> {
            if (!filters.contains(player.getName())) {
                player.addCards(Collections.emptyList());
            }
        });

        return copy(holdemRoundSettingsDTO, playersWithSecureCards);
    }

    public static HoldemRoundSettingsDTO copy(HoldemRoundSettingsDTO holdemRoundSettingsDTO, List<Player> players) {
        return HoldemRoundSettingsDTO.builder()
                .gameName(holdemRoundSettingsDTO.getGameName())
                .bank(holdemRoundSettingsDTO.getBank())
                .smallBlindBet(holdemRoundSettingsDTO.getSmallBlindBet())
                .bigBlindBet(holdemRoundSettingsDTO.getBigBlindBet())
                .bigBlind(holdemRoundSettingsDTO.getBigBlind())
                .smallBlind(holdemRoundSettingsDTO.getSmallBlind())
                .button(holdemRoundSettingsDTO.getButton())
                .players(players)
                .stageType(holdemRoundSettingsDTO.getStageType())
                .lastBet(holdemRoundSettingsDTO.getLastBet())
                .activePlayer(holdemRoundSettingsDTO.getActivePlayer())
                .flop(holdemRoundSettingsDTO.getFlop())
                .river(holdemRoundSettingsDTO.getRiver())
                .tern(holdemRoundSettingsDTO.getTern())
                .build();
    }

}
