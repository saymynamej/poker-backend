package ru.smn.poker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.GameType;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Game {
    private final Long id;
    private final String gameName;
    private final GameType gameType;
    private final List<PlayerEntity> players;
}
