package ru.smn.poker.util;

import ru.smn.poker.dto.HoldemTableSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;

import java.util.Collections;
import java.util.List;

public class RoundSettingsUtil {

    public static TableSettings copyWithSecureCard(TableSettings tableSettings, List<String> filters) {
        final List<PlayerEntity> playersWithSecureCards = PlayerUtil.copies(tableSettings.getPlayers());
        playersWithSecureCards.forEach(player -> {
            if (!filters.contains(player.getName())) {
                player.getTableSettings().addCards(Collections.emptyList());
            }
        });
        return copy(tableSettings, playersWithSecureCards);
    }

    public static TableSettings copy(TableSettings tableSettings, List<PlayerEntity> players) {
        return HoldemTableSettings.builder()
                .tableName(tableSettings.getTableName())
                .bank(tableSettings.getBank())
                .smallBlindBet(tableSettings.getSmallBlindBet())
                .bigBlindBet(tableSettings.getBigBlindBet())
                .bigBlind(tableSettings.getBigBlind())
                .smallBlind(tableSettings.getSmallBlind())
                .button(tableSettings.getButton())
                .players(players)
                .stageType(tableSettings.getStageType())
                .lastBet(tableSettings.getLastBet())
                .activePlayer(tableSettings.getActivePlayer())
                .flop(tableSettings.getFlop())
                .river(tableSettings.getRiver())
                .tern(tableSettings.getTern())
                .build();
    }

}
