package ru.sm.poker.service;

import ru.sm.poker.dto.Player;
import ru.sm.poker.dto.ResultTime;


public interface TimeBankService {

    ResultTime activateTime(Player player);

    void cancel(ResultTime resultTime, Player player);
}
