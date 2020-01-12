package ru.sm.poker.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;

import java.util.List;

@RequiredArgsConstructor
@Data
public class CombinationDTO {
    private final CombinationType combinationType;
    private final List<CardType> cards;
}
