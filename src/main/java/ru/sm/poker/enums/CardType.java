package ru.sm.poker.enums;

import lombok.Getter;

public enum CardType {
    A_D(PowerType.A_POWER, SuitType.DIAMOND),
    K_D(PowerType.K_POWER, SuitType.DIAMOND),
    Q_D(PowerType.Q_POWER, SuitType.DIAMOND),
    J_D(PowerType.J_POWER, SuitType.DIAMOND),
    TEN_D(PowerType.TEN_POWER, SuitType.DIAMOND),
    NINE_D(PowerType.NINE_POWER, SuitType.DIAMOND),
    EIGHT_D(PowerType.EIGHT_POWER, SuitType.DIAMOND),
    SEVEN_D(PowerType.SEVEN_POWER, SuitType.DIAMOND),
    SIX_D(PowerType.SIX_POWER, SuitType.DIAMOND),
    FIVE_D(PowerType.FIVE_POWER, SuitType.DIAMOND),
    FOUR_D(PowerType.FOUR_POWER, SuitType.DIAMOND),
    THREE_D(PowerType.THREE_POWER, SuitType.DIAMOND),
    TWO_D(PowerType.TWO_POWER, SuitType.DIAMOND),
    A_H(PowerType.A_POWER, SuitType.HEART),
    K_H(PowerType.K_POWER, SuitType.HEART),
    Q_H(PowerType.Q_POWER, SuitType.HEART),
    J_H(PowerType.J_POWER, SuitType.HEART),
    TEN_H(PowerType.TEN_POWER, SuitType.HEART),
    NINE_H(PowerType.NINE_POWER, SuitType.HEART),
    EIGHT_H(PowerType.EIGHT_POWER, SuitType.HEART),
    SEVEN_H(PowerType.SEVEN_POWER, SuitType.HEART),
    SIX_H(PowerType.SIX_POWER, SuitType.HEART),
    FIVE_H(PowerType.FIVE_POWER, SuitType.HEART),
    FOUR_H(PowerType.FOUR_POWER, SuitType.HEART),
    THREE_H(PowerType.THREE_POWER, SuitType.HEART),
    TWO_H(PowerType.TWO_POWER, SuitType.HEART),
    A_S(PowerType.A_POWER, SuitType.SPADE),
    K_S(PowerType.K_POWER, SuitType.SPADE),
    Q_S(PowerType.Q_POWER, SuitType.SPADE),
    J_S(PowerType.J_POWER, SuitType.SPADE),
    TEN_S(PowerType.TEN_POWER, SuitType.SPADE),
    NINE_S(PowerType.NINE_POWER, SuitType.SPADE),
    EIGHT_S(PowerType.EIGHT_POWER, SuitType.SPADE),
    SEVEN_S(PowerType.SEVEN_POWER, SuitType.SPADE),
    SIX_S(PowerType.SIX_POWER, SuitType.SPADE),
    FIVE_S(PowerType.FIVE_POWER, SuitType.SPADE),
    FOUR_S(PowerType.FOUR_POWER, SuitType.SPADE),
    THREE_S(PowerType.THREE_POWER, SuitType.SPADE),
    TWO_S(PowerType.TWO_POWER, SuitType.SPADE),
    A_C(PowerType.A_POWER, SuitType.CLUB),
    K_C(PowerType.K_POWER, SuitType.CLUB),
    Q_C(PowerType.Q_POWER, SuitType.CLUB),
    J_C(PowerType.J_POWER, SuitType.CLUB),
    TEN_C(PowerType.TEN_POWER, SuitType.CLUB),
    NINE_C(PowerType.NINE_POWER, SuitType.CLUB),
    EIGHT_C(PowerType.EIGHT_POWER, SuitType.CLUB),
    SEVEN_C(PowerType.SEVEN_POWER, SuitType.CLUB),
    SIX_C(PowerType.SIX_POWER, SuitType.CLUB),
    FIVE_C(PowerType.FIVE_POWER, SuitType.CLUB),
    FOUR_C(PowerType.FOUR_POWER, SuitType.CLUB),
    THREE_C(PowerType.THREE_POWER, SuitType.CLUB),
    TWO_C(PowerType.TWO_POWER, SuitType.CLUB);

    @Getter
    private final PowerType power;
    @Getter
    private final SuitType suitType;

    CardType(PowerType powerType, SuitType suitType) {
        this.power = powerType;
        this.suitType = suitType;
    }

    public int getPowerAsInt(){
        return power.getPowerAsInt();
    }


    public enum SuitType {
        DIAMOND,
        HEART,
        CLUB,
        SPADE
    }
}