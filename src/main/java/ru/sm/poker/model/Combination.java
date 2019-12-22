package ru.sm.poker.model;

import lombok.*;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;

import java.util.List;

@RequiredArgsConstructor
@Data
public class Combination {
    private final CombinationType combinationType;
    private final List<CardType> cards;
}
