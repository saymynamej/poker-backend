package ru.smn.poker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.smn.combination.data.Combination;
import ru.smn.poker.entities.PlayerEntity;

@AllArgsConstructor
@Getter
@Builder
public class PlayerCombination {
    private final PlayerEntity player;
    private final Combination combination;
}
