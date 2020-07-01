package ru.sm.poker.converter;

import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.entities.ChipsCountEntity;
import ru.sm.poker.entities.GameEntity;
import ru.sm.poker.entities.PlayerEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ChipsCountConverter {

    public static List<ChipsCountEntity> toEntities(List<PlayerDTO> players) {
        return players.stream()
                .map(ChipsCountConverter::toEntity)
                .collect(Collectors.toList());
    }

    public static ChipsCountEntity toEntity(GameEntity gameEntity, PlayerEntity playerEntity, long chipsCount) {
        return ChipsCountEntity.builder()
                .count(chipsCount)
                .playerEntity(playerEntity)
                .gameEntity(gameEntity)
                .build();
    }

    public static ChipsCountEntity toEntity(PlayerDTO playerDTO) {
        return ChipsCountEntity.builder()
                .count(playerDTO.getChipsCount())
                .playerEntity(PlayerConverter.toEntity(playerDTO))
                .gameEntity(GameEntity.builder().build())
                .build();
    }
}
