package ru.sm.poker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.sm.poker.enums.GameType;

@RequiredArgsConstructor
@Data
@Builder
public class GameDTO {
    private final String name;
    private final GameType gameType;
    private final int countPlayers;
    private final int maxPlayersSize;
}
