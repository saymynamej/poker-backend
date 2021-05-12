package ru.smn.combination.strategy.power;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.PowerType;
import ru.smn.combination.utils.CardUtils;

import java.util.List;

class KareCalculatePowerStrategy implements CalculatePowerStrategy {

    @Override
    public int calc(List<CardType> combination) {
        final PowerType karePower = CardUtils.findPowerOfCardWithFilter(combination, (entry) -> entry.getValue() == 4)
                .orElseThrow();

        final PowerType highCardPower = CardUtils.findPowerOfCardWithFilter(combination, (entry) -> entry.getValue() == 1)
                .orElseThrow();

        return karePower.getPowerAsInt() + highCardPower.getPowerAsInt();
    }
}
