package ru.sm.poker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WinnerDTO {
    private final Player player;
    private final CombinationDTO combinationDTO;
}
