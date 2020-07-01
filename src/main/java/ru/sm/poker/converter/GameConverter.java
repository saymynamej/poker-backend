package ru.sm.poker.converter;

import org.springframework.stereotype.Service;
import ru.sm.poker.dto.GameDTO;
import ru.sm.poker.game.Game;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GameConverter {
    public GameDTO toGameDTO(Game game) {
        return GameDTO.builder()
                .name(game.getGameName())
                .gameType(game.getGameSettings().getGameType())
                .countPlayers(game.getPlayers().size())
                .maxPlayersSize(game.getGameSettings().getMaxPlayerSize())
                .build();

    }

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
}
