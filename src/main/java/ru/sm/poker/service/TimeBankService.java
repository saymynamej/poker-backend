package ru.sm.poker.service;

import org.springframework.stereotype.Service;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.Player;

import java.util.Timer;
import java.util.TimerTask;

@Service
public class TimeBankService {

    private final static long DEFAULT_TIME_FOR_ACTION = 20;


    public Timer activateTimeBank(Player player) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.setStateType(StateType.AFK);
            }
        }, player.getTimeBank() * 1000L);
        return timer;
    }

}