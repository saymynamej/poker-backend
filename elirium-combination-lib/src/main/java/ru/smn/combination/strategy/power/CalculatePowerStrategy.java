package ru.smn.combination.strategy.power;

import ru.smn.combination.data.CardType;

import java.util.List;

interface CalculatePowerStrategy {
    int calc(List<CardType> combination);
}
