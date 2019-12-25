package ru.sm.poker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Builder
public class GameDTO {
    private final String name;
    private final int countPlayers;
}
