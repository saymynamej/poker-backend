package ru.sm.poker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PlayerCombinationDTO {
    private final Player player;
    private final CombinationDTO combination;
}
