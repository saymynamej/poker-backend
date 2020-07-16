package ru.smn.poker.util;

import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;
import ru.smn.poker.game.RoundSettings;

import java.util.Collections;
import java.util.List;

public class RoundSettingsUtil {

    public static RoundSettings copyWithSecureCard(RoundSettings roundSettings, List<String> filters) {
        final List<Player> playersWithSecureCards = PlayerUtil.copies(roundSettings.getPlayers());
        playersWithSecureCards.forEach(player -> {
            if (!filters.contains(player.getName())) {
                player.addCards(Collections.emptyList());
            }
        });

        return copy(roundSettings, playersWithSecureCards);
    }

    public static RoundSettings copy(RoundSettings roundSettings, List<Player> players) {
        return HoldemRoundSettings.builder()
                .gameName(roundSettings.getGameName())
                .bank(roundSettings.getBank())
                .smallBlindBet(roundSettings.getSmallBlindBet())
                .bigBlindBet(roundSettings.getBigBlindBet())
                .bigBlind(roundSettings.getBigBlind())
                .smallBlind(roundSettings.getSmallBlind())
                .button(roundSettings.getButton())
                .players(players)
                .stageType(roundSettings.getStageType())
                .lastBet(roundSettings.getLastBet())
                .activePlayer(roundSettings.getActivePlayer())
                .flop(roundSettings.getFlop())
                .river(roundSettings.getRiver())
                .tern(roundSettings.getTern())
                .build();
    }

}
