package ru.sm.poker.converter;

import ru.sm.poker.dto.GameDTO;
import ru.sm.poker.model.GameEntity;
import ru.sm.poker.model.PlayerEntity;

import java.util.List;
import java.util.stream.Collectors;


public class GameConverter {

    public static List<GameEntity> toEntities(List<GameDTO> games) {
        return games.stream().map(GameConverter::toEntity)
                .collect(Collectors.toList());
    }

    public static GameEntity toEntity(GameDTO gameDTO) {
        final GameEntity gameEntity = GameEntity.builder()
                .id(gameDTO.getId())
                .gameType(gameDTO.getGameType())
                .build();
        final List<PlayerEntity> converterPlayerEntities = PlayerConverter.toEntities(gameDTO.getPlayers());
        gameEntity.setPlayers(converterPlayerEntities);
        return gameEntity;
    }

}
