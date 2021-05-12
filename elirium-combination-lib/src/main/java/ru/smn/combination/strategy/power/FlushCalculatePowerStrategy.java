package ru.smn.combination.strategy.power;

import ru.smn.combination.data.CardType;
import ru.smn.combination.utils.CardUtils;

import java.util.List;

class FlushCalculatePowerStrategy implements CalculatePowerStrategy {

    @Override
    public int calc(List<CardType> combination) {
        return CardUtils.findTheBiggestCard(combination).getPowerAsInt();
    }
}
