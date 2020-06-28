package ru.sm.poker.dto;

import lombok.*;
import ru.sm.poker.enums.GameType;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class GameDTO {
    private final Long id;
    private final GameType gameType;
    private final List<PlayerDTO> players;
}
