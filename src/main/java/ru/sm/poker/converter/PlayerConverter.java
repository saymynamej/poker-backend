package ru.sm.poker.converter;

import ru.sm.poker.dto.GameDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.model.CardEntity;
import ru.sm.poker.model.GameEntity;
import ru.sm.poker.model.PlayerEntity;

import java.util.List;
import java.util.stream.Collectors;


public class PlayerConverter {

    public static List<PlayerEntity> toEntities(List<PlayerDTO> players){
        return players.stream().map(PlayerConverter::toEntity)
                .collect(Collectors.toList());
    }

    public static PlayerEntity toEntity(PlayerDTO playerDTO) {
        final PlayerEntity playerEntity = PlayerEntity.builder()
                .name(playerDTO.getName())
                .roleType(playerDTO.getRoleType())
                .build();
        final List<GameEntity> gameEntities = GameConverter.toEntities(playerDTO.getGames());
        playerEntity.setGames(gameEntities);

        //TODO need adapt it to mutli tables
        final List<CardEntity> cardEntities = CardConverter.toEntities(playerDTO.getCards(), playerEntity, gameEntities.get(0));
        playerEntity.setCards(cardEntities);
        return playerEntity;
    }

    public static List<PlayerDTO> toDTOs(List<PlayerEntity> playerEntities) {
        return playerEntities.stream()
                .map(PlayerConverter::toDTO)
                .collect(Collectors.toList());
    }

    public static PlayerDTO toDTO(PlayerEntity playerEntity) {
        return PlayerDTO.builder()
                .name(playerEntity.getName())
                .build();
    }
}
