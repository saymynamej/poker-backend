package ru.sm.poker.util;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;

import java.util.Collections;
import java.util.List;

public class RoundSettingsUtil {

    public static RoundSettingsDTO copyWithSecureCard(RoundSettingsDTO roundSettingsDTO, String filterName) {

        final List<Player> playersWithSecureCards = PlayerUtil.copies(roundSettingsDTO.getPlayers());

        playersWithSecureCards.forEach(player ->  {
            if (!player.getName().equals(filterName)){
                player.addCards(Collections.emptyList());
            }
        });

        return RoundSettingsDTO.builder()
                .gameName(roundSettingsDTO.getGameName())
                .bank(roundSettingsDTO.getBank())
                .smallBlindBet(roundSettingsDTO.getSmallBlindBet())
                .bigBlindBet(roundSettingsDTO.getBigBlindBet())
                .bigBlind(roundSettingsDTO.getBigBlind())
                .smallBlind(roundSettingsDTO.getSmallBlind())
                .button(roundSettingsDTO.getButton())
                .players(playersWithSecureCards)
                .stageType(roundSettingsDTO.getStageType())
                .lastBet(roundSettingsDTO.getLastBet())
                .activePlayer(roundSettingsDTO.getActivePlayer())
                .flop(roundSettingsDTO.getFlop())
                .river(roundSettingsDTO.getRiver())
                .tern(roundSettingsDTO.getTern())
                .build();
    }

}
