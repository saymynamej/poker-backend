package ru.smn.poker.converter;

import ru.smn.poker.dto.Card;
import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.PlayerSettingsEntity;

import java.util.List;
import java.util.stream.Collectors;


public class PlayerConverter {

    public static List<Player> toDTOs(List<PlayerEntity> playerEntities) {
        return playerEntities.stream()
                .map(PlayerConverter::toDTO)
                .collect(Collectors.toList());
    }

    public static Player toDTO(PlayerEntity playerEntity) {
        if (playerEntity == null) {
            return null;
        }
        final PlayerSettingsEntity tableSettings = playerEntity.getTableSettings();
        return Player.builder()
                .chipsCount(tableSettings.getChipsCount().getCount())
                .cards(tableSettings.getCards() == null ? null : tableSettings.getCards().stream()
                        .map(cardEntity -> Card.builder()
                                .id(cardEntity.getId())
                                .cardType(cardEntity.getCardType())
                                .build())
                        .collect(Collectors.toList()))
                .chipsId(tableSettings.getChipsCount().getId())
                .id(playerEntity.getId())
                .timeBank(tableSettings.getTimeBank())
                .active(tableSettings.isActive())
                .roleType(tableSettings.getRoleType())
                .action(playerEntity.getAction())
                .name(playerEntity.getName())
                .build();
    }
}
