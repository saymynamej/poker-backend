package ru.sm.poker.converter;

import ru.sm.poker.dto.Game;
import ru.sm.poker.dto.Player;
import ru.sm.poker.entities.GameEntity;
import ru.sm.poker.entities.PlayerEntity;

import java.util.List;
import java.util.stream.Collectors;


public class GameConverter {

    public static List<GameEntity> toEntities(List<Game> games) {
        return games.stream().map(GameConverter::toEntity)
                .collect(Collectors.toList());
    }

    public static GameEntity toEntity(ru.sm.poker.game.Game game){
        return toEntity(Game.builder()
                .gameName(game.getGameName())
                .gameType(game.getGameSettings().getGameType())
                .players(game.getPlayers())
                .build());
    }

    public static GameEntity toEntity(Game game) {
        final GameEntity gameEntity = GameEntity.builder()
                .gameType(game.getGameType())
                .name(game.getGameName())
                .build();

        if (game.getId() != null) {
            gameEntity.setId(game.getId());
        }

        final List<PlayerEntity> playerEntities = PlayerConverter.toEntities(
                game.getPlayers(),
                gameEntity
        );

        gameEntity.setPlayers(playerEntities);
        return gameEntity;
    }


    private static Long getChipsByName(List<Player> players, String name) {
        return players.stream()
                .filter(player -> player.getName().equals(name))
                .findAny()
                .orElseThrow()
                .getChipsCount();
    }
}
