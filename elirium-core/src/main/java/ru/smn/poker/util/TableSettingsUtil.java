package ru.smn.poker.util;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;

import java.util.Collections;
import java.util.List;

public class TableSettingsUtil {

    public static TableSettings copyWithSecureCard(TableSettings tableSettings, List<String> filters) {
        final List<PlayerEntity> playersWithSecureCards = PlayerUtil.copies(tableSettings.getPlayers());
        playersWithSecureCards.forEach(player -> {
            if (!filters.contains(player.getName())) {
                player.getTableSettings().addCards(Collections.emptyList());
            }
        });
        return tableSettings.copy(playersWithSecureCards);
    }

}
