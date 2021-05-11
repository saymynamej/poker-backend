package ru.smn.combination.strategy.power;

import ru.smn.combination.data.CardType;
import ru.smn.combination.utils.CardUtils;

import java.util.List;

public class StraightCalculatePowerStrategy implements CalculatePowerStrategy {
    @Override
    public int calc(List<CardType> combination) {
        if (!CardUtils.checkStraitWithAce(combination).isEmpty()) {
            return 5;
        }
        return CardUtils.findBiggerCard(combination).getPowerAsInt();
    }
}
