package ru.sm.poker.converter;

import ru.sm.poker.dto.GameInfo;
import ru.sm.poker.game.Game;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GameInfoConverter {

    public static List<GameInfo> toDTOs(Set<Game> games) {
        return games.stream()
                .map(game -> GameInfo.builder()
                        .name(game.getGameName())
                        .maxPlayersSize(game.getGameSettings().getMaxPlayerSize())
                        .gameType(game.getGameSettings().getGameType())
                        .countPlayers(game.getPlayers().size())
                        .build())
                .collect(Collectors.toList());
    }

}
