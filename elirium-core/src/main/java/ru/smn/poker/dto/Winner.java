package ru.smn.poker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.smn.combination.data.Combination;

@AllArgsConstructor
@Getter
public class Winner {
    private final Player player;
    private final Combination combination;
}
