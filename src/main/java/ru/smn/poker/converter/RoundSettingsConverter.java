package ru.smn.poker.converter;

import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.GameType;

import java.util.List;
import java.util.stream.Collectors;

public class RoundSettingsConverter {


    public static GameEntity toEntity(HoldemRoundSettings holdemRoundSettings) {
        final GameEntity gameEntity = GameEntity.builder()
                .id(holdemRoundSettings.getGameId())
                .name(holdemRoundSettings.getGameName())
                .gameType(GameType.HOLDEM_FULL)
                .build();

        final List<Player> players = holdemRoundSettings.getPlayers();

        final List<PlayerEntity> playerEntities = PlayerConverter.toEntities(players, gameEntity);

        gameEntity.setPlayers(playerEntities);

        final List<ChipsCountEntity> chipsCountEntities = playerEntities.stream()
                .map(PlayerEntity::getChipsCount)
                .collect(Collectors.toList());

        gameEntity.setCounts(chipsCountEntities);

        return gameEntity;
    }
}
