package ru.smn.poker.service;

import ru.smn.poker.dto.Player;
import ru.smn.poker.dto.ResultTime;


public interface TimeBankService {

    ResultTime activateTime(Player player);

    void cancel(ResultTime resultTime, Player player);
}
