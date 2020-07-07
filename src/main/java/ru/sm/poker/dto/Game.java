package ru.sm.poker.dto;

import lombok.*;
import ru.sm.poker.enums.GameType;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Game {
    private final Long id;
    private final String gameName;
    private final GameType gameType;
    private final List<Player> players;
}
