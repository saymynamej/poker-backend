package ru.smn.combination.strategy.power;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.PowerType;
import ru.smn.combination.utils.CardUtils;

import java.util.List;

public class ThreeCalculatePowerStrategy implements CalculatePowerStrategy {
    @Override
    public int calc(List<CardType> combination) {
        final PowerType powerType = CardUtils.findPowerOfCardWithFilter(combination, (entry) -> entry.getValue() == 3)
                .orElseThrow();

        return powerType.getPowerAsInt();
    }
}
