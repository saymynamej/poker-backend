package ru.sm.poker.converter;

import ru.sm.poker.dto.GameInfoDTO;
import ru.sm.poker.game.Game;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameInfoConverter {

    public static List<GameInfoDTO> toDTOs(Map<String, Game> games) {
        return games.values().stream()
                .map(game -> GameInfoDTO.builder()
                        .name(game.getGameName())
                        .maxPlayersSize(game.getGameSettings().getMaxPlayerSize())
                        .gameType(game.getGameSettings().getGameType())
                        .countPlayers(game.getPlayers().size())
                        .build())
                .collect(Collectors.toList());
    }

}
