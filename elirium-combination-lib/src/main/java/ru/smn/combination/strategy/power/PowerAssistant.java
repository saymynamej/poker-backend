package ru.smn.combination.strategy.power;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.CombinationType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PowerAssistant {
    private final static Map<CombinationType, CalculatePowerStrategy> calculatePowerStrategies = new HashMap<>();

    static {
        calculatePowerStrategies.put(CombinationType.HIGH_CARD, new HighCardCalculatePowerStrategy());
        calculatePowerStrategies.put(CombinationType.ONE_PAIR, new OnePairCalculatePowerStrategy());
        calculatePowerStrategies.put(CombinationType.TWO_PAIR, new TwoPairCalculatePowerStrategy());
        calculatePowerStrategies.put(CombinationType.THREE_CARDS, new ThreeCalculatePowerStrategy());
        calculatePowerStrategies.put(CombinationType.STRAIGHT, new StraightCalculatePowerStrategy());
        calculatePowerStrategies.put(CombinationType.FLUSH, new FlushCalculatePowerStrategy());
        calculatePowerStrategies.put(CombinationType.FULL_HOUSE, new FullHouseCalculatePowerStrategy());
        calculatePowerStrategies.put(CombinationType.KARE, new KareCalculatePowerStrategy());
        calculatePowerStrategies.put(CombinationType.STRAIGHT_FLUSH, new StraightFlushCalculatePowerStrategy());
        calculatePowerStrategies.put(CombinationType.FLUSH_ROYAL, new FlushRoyalCalculatePowerStrategy());
    }

    public static int calc(List<CardType> cards, CombinationType combinationType) {
        return calculatePowerStrategies.get(combinationType).calc(cards);
    }
}
