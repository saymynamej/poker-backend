package ru.sm.poker.converter;

import ru.sm.poker.dto.GameDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.entities.ChipsCountEntity;
import ru.sm.poker.entities.GameEntity;
import ru.sm.poker.entities.PlayerEntity;

import java.util.List;
import java.util.stream.Collectors;


public class GameConverter {

    public static List<GameEntity> toEntities(List<GameDTO> games) {
        return games.stream().map(GameConverter::toEntity)
                .collect(Collectors.toList());
    }

    public static GameEntity toEntity(Game game){
        return toEntity(GameDTO.builder()
                .gameName(game.getGameName())
                .gameType(game.getGameSettings().getGameType())
                .players(game.getPlayers())
                .build());
    }

    public static GameEntity toEntity(GameDTO gameDTO) {
        final GameEntity gameEntity = GameEntity.builder()
                .gameType(gameDTO.getGameType())
                .name(gameDTO.getGameName())
                .id(gameDTO.getId())
                .build();

        final List<PlayerDTO> players = gameDTO.getPlayers();

        final List<PlayerEntity> playerEntities = PlayerConverter.toEntities(players);

        gameEntity.setPlayers(playerEntities);

        final List<ChipsCountEntity> chipsCountEntities = playerEntities.stream()
                .map( playerEntity ->  {
                    final ChipsCountEntity chipsCountEntity = ChipsCountConverter.toEntity(
                            gameEntity,
                            playerEntity,
                            getChipsByName(players, playerEntity.getName()));
                    playerEntity.setChipsCount(chipsCountEntity);
                    playerEntity.setGames(gameEntity);
                    return chipsCountEntity;
                })
                .collect(Collectors.toList());

        gameEntity.setCounts(chipsCountEntities);

        return gameEntity;
    }


    private static Long getChipsByName(List<PlayerDTO> players, String name) {
        return players.stream()
                .filter(player -> player.getName().equals(name))
                .findAny()
                .orElseThrow()
                .getChipsCount();
    }
}
