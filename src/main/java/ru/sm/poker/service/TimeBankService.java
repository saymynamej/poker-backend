package ru.sm.poker.service;

import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.dto.ResultTimeDTO;


public interface TimeBankService {

    ResultTimeDTO activateTime(PlayerDTO player);

    void cancel(ResultTimeDTO resultTimeDTO, PlayerDTO player);
}
