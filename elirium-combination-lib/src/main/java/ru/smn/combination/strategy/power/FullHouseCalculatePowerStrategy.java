package ru.smn.combination.strategy.power;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.PowerType;
import ru.smn.combination.utils.CardUtils;

import java.util.List;

public class FullHouseCalculatePowerStrategy implements CalculatePowerStrategy {

    @Override
    public int calc(List<CardType> combination) {
        final PowerType threeCardPower = CardUtils.findPowerOfCardWithFilter(combination, (entry) -> entry.getValue() == 3)
                .stream()
                .findAny()
                .orElseThrow();

        final PowerType twoCardPower = CardUtils.findPowerOfCardWithFilter(combination, (entry) -> entry.getValue() == 2).stream()
                .findAny()
                .orElseThrow();

        return threeCardPower.getPowerAsInt() + twoCardPower.getPowerAsInt();
    }
}
