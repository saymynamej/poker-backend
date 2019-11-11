package ru.sm.poker.enums;

public enum Combination {
    FLUSH_ROYAL(10),
    STRAIT_FLUSH(9),
    KARE(8),
    FULL_HOUSE(7),
    FLUSH(6),
    STRAIT(5),
    THREE(4),
    TWO_PAIR(3),
    PAIR(2),
    HIGH_CARD(1);




    private final int power;

    Combination(int power) {
        this.power = power;
    }

    public int getPower() {
        return power;
    }
}
