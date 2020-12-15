package ru.smn.poker.converter;

import ru.smn.poker.dto.Game;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.game.RoundSettings;


public class GameConverter {

    public static GameEntity toEntity(ru.smn.poker.game.Game game) {
        return toEntity(Game.builder()
                        .gameName(game.getGameName())
                        .gameType(game.getGameSettings().getGameType())
                        .players(game.getPlayers())
                        .build(),
                game.getRoundSettings());
    }

    public static GameEntity toEntity(Game game, RoundSettings roundSettings) {
        final GameEntity gameEntity = GameEntity.builder()
                .gameType(game.getGameType())
                .name(game.getGameName())
                .build();

        if (game.getId() != null) {
            gameEntity.setId(game.getId());
        }

        gameEntity.setPlayers(game.getPlayers());


        return gameEntity;
    }

}
