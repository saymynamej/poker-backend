package ru.smn.poker.converter;

import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.GameEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ChipsCountConverter {

    public static List<ChipsCountEntity> toEntities(List<Player> players, GameEntity gameEntity) {
        return players.stream()
                .map(playerDTO -> toEntity(playerDTO, gameEntity))
                .collect(Collectors.toList());
    }


    public static ChipsCountEntity toEntity(Player player, GameEntity gameEntity) {
        final ChipsCountEntity chipsCountEntity = ChipsCountEntity.builder()
                .game(gameEntity)
                .count(player.getChipsCount())
                .build();
        if (player.getChipsId() != 0){
            chipsCountEntity.setId(player.getChipsId());
        }
        return chipsCountEntity;

    }
}
