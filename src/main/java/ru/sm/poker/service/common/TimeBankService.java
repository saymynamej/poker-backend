package ru.sm.poker.service.common;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.sm.poker.action.holdem.Fold;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.StateType;

import java.util.Timer;
import java.util.TimerTask;

@Service
public class TimeBankService {

    public Pair<Timer, Long> activateTimeBank(Player player) {
        final long startTime = System.currentTimeMillis();
        final Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.setStateType(StateType.AFK);
                player.setAction(new Fold());
            }
        }, player.getTimeBank() * 1000L);
        return Pair.of(timer, startTime);
    }

    public void cancel(long startTime, Player player, Timer timer){
      timer.cancel();
      final long endTime = System.currentTimeMillis();
      final long result = (endTime - startTime) / 1000;
      player.setTimeBank(player.getTimeBank() - result);
    }

}
