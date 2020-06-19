package ru.sm.poker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WinnerDTO {
    private final PlayerDTO player;
    private final CombinationDTO combinationDTO;
}
