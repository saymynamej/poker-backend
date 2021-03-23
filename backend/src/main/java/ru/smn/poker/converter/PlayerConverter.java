package ru.smn.poker.converter;

import ru.smn.poker.dto.Card;
import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.PlayerEntity;

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
        return Player.builder()
//                .chipsCount(playerEntity.getSettings().getChipsCount().getCount())
//                .cards(playerEntity.getSettings().getCards() == null ? null : playerEntity.getSettings().getCards().stream()
//                        .map(cardEntity -> Card.builder()
//                                .id(cardEntity.getId())
//                                .cardType(cardEntity.getCardType())
//                                .build())
//                        .collect(Collectors.toList()))
//                .chipsId(playerEntity.getSettings().getChipsCount().getId())
//                .id(playerEntity.getId())
//                .timeBank(playerEntity.getTimeBank())
//                .active(playerEntity.getSettings().isActive())
//                .roleType(playerEntity.getSettings().getRoleType())
//                .action(playerEntity.getAction())
//                .name(playerEntity.getName())
                .build();
    }
}
