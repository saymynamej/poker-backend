package ru.sm.poker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;

import java.util.List;

@RequiredArgsConstructor
@Data
@EqualsAndHashCode(of = "combinationType")
@Builder
public class Combination {
    private final CombinationType combinationType;
    private final List<CardType> cards;

    public static Combination of(CombinationType type, List<CardType> cards){
        return Combination.builder()
                .combinationType(type)
                .cards(cards)
                .build();
    }
}