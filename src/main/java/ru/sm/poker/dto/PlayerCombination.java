package ru.sm.poker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PlayerCombination {
    private final Player player;
    private final Combination combination;
}
