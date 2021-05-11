package ru.smn.combination.strategy.power;

import ru.smn.combination.data.CardType;
import ru.smn.combination.utils.CardUtils;

import java.util.List;

public class FlushRoyalCalculatePowerStrategy implements CalculatePowerStrategy {

    @Override
    public int calc(List<CardType> combination) {
        return CardUtils.findBiggerCard(combination).getPowerAsInt();
    }
}
