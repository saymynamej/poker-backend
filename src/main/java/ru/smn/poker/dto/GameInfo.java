package ru.smn.poker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.smn.poker.enums.GameType;

@RequiredArgsConstructor
@Data
@Builder
public class GameInfo {
    private final String name;
    private final GameType gameType;
    private final int countPlayers;
    private final int maxPlayersSize;
}
