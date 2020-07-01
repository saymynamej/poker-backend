package ru.sm.poker.converter;

import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.entities.CardEntity;
import ru.sm.poker.entities.GameEntity;
import ru.sm.poker.entities.PlayerEntity;

import java.util.List;
import java.util.stream.Collectors;


public class PlayerConverter {

    public static List<PlayerEntity> toEntities(List<PlayerDTO> players) {
        return players.stream().map(PlayerConverter::toEntity)
                .collect(Collectors.toList());
    }

    public static PlayerEntity toEntity(PlayerDTO playerDTO) {
        final PlayerEntity playerEntity = PlayerEntity.builder()
                .name(playerDTO.getName())
                .roleType(playerDTO.getRoleType())
                .build();
        if (playerDTO.getId() != null){
            playerEntity.setId(playerDTO.getId());
        }
        if (playerDTO.getGame() != null) {
            final GameEntity gameEntities = GameConverter.toEntity(playerDTO.getGame());
            playerEntity.setGames(gameEntities);
            final List<CardEntity> cardEntities = CardConverter.toEntities(
                    playerDTO.getCards(),
                    playerEntity,
                    gameEntities);
            playerEntity.setCards(cardEntities);
        }
        return playerEntity;
    }

    public static List<PlayerDTO> toDTOs(List<PlayerEntity> playerEntities) {
        return playerEntities.stream()
                .map(PlayerConverter::toDTO)
                .collect(Collectors.toList());
    }

    public static PlayerDTO toDTO(PlayerEntity playerEntity) {
        return PlayerDTO.builder()
                .chipsCount(playerEntity.getChipsCount().getCount())
                .id(playerEntity.getId())
                .name(playerEntity.getName())
                .build();
    }
}
