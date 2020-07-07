package ru.sm.poker.util;

import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;

import java.util.Collections;
import java.util.List;

public class RoundSettingsUtil {

    public static HoldemRoundSettings copyWithSecureCard(HoldemRoundSettings holdemRoundSettings, List<String> filters) {
        final List<Player> playersWithSecureCards = PlayerUtil.copies(holdemRoundSettings.getPlayers());
        playersWithSecureCards.forEach(player -> {
            if (!filters.contains(player.getName())) {
                player.addCards(Collections.emptyList());
            }
        });

        return copy(holdemRoundSettings, playersWithSecureCards);
    }

    public static HoldemRoundSettings copy(HoldemRoundSettings holdemRoundSettings, List<Player> players) {
        return HoldemRoundSettings.builder()
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
