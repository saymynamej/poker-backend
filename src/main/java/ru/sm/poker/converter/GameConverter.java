package ru.sm.poker.converter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.GameDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.GameEntity;
import ru.sm.poker.model.PlayerEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameConverter {
    private final PlayerConverter playerConverter;

    public List<GameDTO> convertGamesMapToList(Map<String, Game> games) {
        return games.values().stream()
                .map(game -> GameDTO.builder()
                        .name(game.getGameName())
                        .maxPlayersSize(game.getGameSettings().getMaxPlayerSize())
                        .gameType(game.getGameSettings().getGameType())
                        .countPlayers(game.getPlayers().size())
                        .build())
                .collect(Collectors.toList());
    }

    public GameEntity convertGameToGameEntity(Game game, List<PlayerDTO> players) {
        final GameEntity gameEntity = GameEntity.builder()
                .gameType(game.getGameSettings().getGameType())
                .build();

        final List<PlayerEntity> converterPlayerEntities = players.stream()
                .map(playerDTO -> playerConverter.convertPlayerDTOToPlayer(playerDTO, Collections.singletonList(gameEntity)))
                .collect(Collectors.toList());
        gameEntity.setPlayers(converterPlayerEntities);
        return gameEntity;
    }
}
