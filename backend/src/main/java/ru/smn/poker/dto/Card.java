package ru.smn.poker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.smn.poker.enums.CardType;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class Card {
    private final Long id;
    private final CardType cardType;
}
