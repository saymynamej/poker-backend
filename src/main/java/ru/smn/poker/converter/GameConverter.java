package ru.smn.poker.converter;

import ru.smn.poker.dto.Game;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.game.TableSettings;

import java.util.Collections;


public class GameConverter {

    public static GameEntity toEntity(ru.smn.poker.game.Game game) {
        return toEntity(Game.builder()
                        .gameName(game.getGameName())
                        .gameType(game.getGameSettings().getGameType())
                        .players(game.getPlayers())
                        .build(),
                game.getTableSettings());
    }

    public static GameEntity toEntity(Game game, TableSettings tableSettings) {
        final GameEntity gameEntity = GameEntity.builder()
                .gameType(game.getGameType())
                .name(game.getGameName())
                .build();

        TableEntity tableEntity = TableEntity.builder()
                .players(game.getPlayers())
                .build();

        if (game.getId() != null) {
            gameEntity.setId(game.getId());
        }

        gameEntity.setTables(Collections.singletonList(tableEntity));



        return gameEntity;
    }

}
