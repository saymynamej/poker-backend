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

    private final static long MILLISECONDS_IN_SECONDS = 1000L;
    private final static long DEFAULT_TIME_FOR_ACTION = 15L;

    public Pair<Timer, Result> activateTime(Player player) {
        final Timer timer = new Timer();
        final Result result = new Result();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fillResult(result);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        setInActivePlayer(player);
                    }
                }, player.getTimeBank() * MILLISECONDS_IN_SECONDS);
            }
        }, DEFAULT_TIME_FOR_ACTION * MILLISECONDS_IN_SECONDS);

        return Pair.of(timer, result);
    }

    private void fillResult(Result result){
        final long startTime = System.currentTimeMillis();
        result.isDone = true;
        result.startTime = startTime;
    }

    private void setInActivePlayer(Player player){
        player.setAction(new Fold());
        player.setStateType(StateType.AFK);
        player.setTimeBank(0L);
    }

    public void cancel(Result result, Player player, Timer timer) {
        timer.cancel();
        if (result.isDone){
            final long startTime = result.startTime;
            final long endTime = System.currentTimeMillis();
            final long calculatedTime = (endTime - startTime) / MILLISECONDS_IN_SECONDS;
            player.setTimeBank(player.getTimeBank() - calculatedTime);
        }
    }

    public static class Result {
        private boolean isDone = false;
        private long startTime;
    }

}
