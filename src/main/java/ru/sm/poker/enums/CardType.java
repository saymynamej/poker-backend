package ru.sm.poker.enums;

import lombok.Getter;

public enum CardType {
    A_D(14, SuitType.DIAMOND),
    K_D(13, SuitType.DIAMOND),
    Q_D(12, SuitType.DIAMOND),
    J_D(11, SuitType.DIAMOND),
    TEN_D(10, SuitType.DIAMOND),
    NINE_D(9, SuitType.DIAMOND),
    EIGHT_D(8, SuitType.DIAMOND),
    SEVEN_D(7, SuitType.DIAMOND),
    SIX_D(6, SuitType.DIAMOND),
    FIVE_D(5, SuitType.DIAMOND),
    FOUR_D(4, SuitType.DIAMOND),
    THREE_D(3, SuitType.DIAMOND),
    TWO_D(2, SuitType.DIAMOND),
    A_H(14, SuitType.HEART),
    K_H(13, SuitType.HEART),
    Q_H(12, SuitType.HEART),
    J_H(11, SuitType.HEART),
    TEN_H(10, SuitType.HEART),
    NINE_H(9, SuitType.HEART),
    EIGHT_H(8, SuitType.HEART),
    SEVEN_H(7, SuitType.HEART),
    SIX_H(6, SuitType.HEART),
    FIVE_H(5, SuitType.HEART),
    FOUR_H(4, SuitType.HEART),
    THREE_H(3, SuitType.HEART),
    TWO_H(2, SuitType.HEART),
    A_S(14, SuitType.SPADE),
    K_S(13, SuitType.SPADE),
    Q_S(12, SuitType.SPADE),
    J_S(11, SuitType.SPADE),
    TEN_S(10, SuitType.SPADE),
    NINE_S(9, SuitType.SPADE),
    EIGHT_S(8, SuitType.SPADE),
    SEVEN_S(7, SuitType.SPADE),
    SIX_S(6, SuitType.SPADE),
    FIVE_S(5, SuitType.SPADE),
    FOUR_S(4, SuitType.SPADE),
    THREE_S(3, SuitType.SPADE),
    TWO_S(2, SuitType.SPADE),
    A_C(14, SuitType.CLUB),
    K_C(13, SuitType.CLUB),
    Q_C(12, SuitType.CLUB),
    J_C(11, SuitType.CLUB),
    TEN_C(10, SuitType.CLUB),
    NINE_C(9, SuitType.CLUB),
    EIGHT_C(8, SuitType.CLUB),
    SEVEN_C(7, SuitType.CLUB),
    SIX_C(6, SuitType.CLUB),
    FIVE_C(5, SuitType.CLUB),
    FOUR_C(4, SuitType.CLUB),
    THREE_C(3, SuitType.CLUB),
    TWO_C(2, SuitType.CLUB);

    @Getter
    private final int power;
    @Getter
    private final SuitType suitType;

    CardType(int power, SuitType suitType) {
        this.power = power;
        this.suitType = suitType;
    }


    public enum SuitType {
        DIAMOND,
        HEART,
        CLUB,
        SPADE
    }
}