package ru.smn.poker.service;

import ru.smn.poker.dto.ResultTime;
import ru.smn.poker.entities.PlayerEntity;


public interface TimeBankService {

    ResultTime activateTime(PlayerEntity player);

    void cancel(ResultTime resultTime, PlayerEntity player);
}
