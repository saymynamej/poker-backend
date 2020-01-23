package ru.sm.poker.service.common;

import org.springframework.stereotype.Service;
import ru.sm.poker.action.holdem.Fold;
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
                player.setAction(new Fold());
            }
        }, player.getTimeBank() * 1000L);
        return timer;
    }

    public void setTimeBank(Player player, long seconds){
        player.setTimeBank(seconds);
    }

}
