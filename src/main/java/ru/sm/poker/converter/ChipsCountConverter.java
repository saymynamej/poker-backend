package ru.sm.poker.converter;

import ru.sm.poker.dto.Player;
import ru.sm.poker.entities.ChipsCountEntity;
import ru.sm.poker.entities.GameEntity;
import ru.sm.poker.entities.PlayerEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ChipsCountConverter {

    public static List<ChipsCountEntity> toEntities(List<Player> players, GameEntity gameEntity) {
        return players.stream()
                .map(playerDTO -> ChipsCountConverter.toEntity(playerDTO, gameEntity))
                .collect(Collectors.toList());
    }

    public static ChipsCountEntity toEntity(GameEntity gameEntity, PlayerEntity playerEntity, long chipsCount) {
        return ChipsCountEntity.builder()
                .count(chipsCount)
                .game(gameEntity)
                .build();
    }

    public static ChipsCountEntity toEntity(Player player, GameEntity gameEntity) {
        return ChipsCountEntity.builder()
                .game(gameEntity)
                .count(player.getChipsCount())
                .build();
    }
}
