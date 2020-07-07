package ru.sm.poker.service.common;

import org.springframework.stereotype.Service;
import ru.sm.poker.dto.Player;
import ru.sm.poker.dto.ResultTime;
import ru.sm.poker.service.TimeBankService;

import java.util.Timer;
import java.util.TimerTask;

@Service
public class SimpleTimeBankService implements TimeBankService {
    private final static long MILLISECONDS_IN_SECONDS = 1000L;
    private final static long DEFAULT_TIME_FOR_ACTION = 15L;

    @Override
    public ResultTime activateTime(Player player) {
        final Timer timer = new Timer();
        final ResultTime result = new ResultTime();
        result.setTimer(timer);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fillResult(result);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        player.setInActive();
                    }
                }, player.getTimeBank() * MILLISECONDS_IN_SECONDS);
            }
        }, DEFAULT_TIME_FOR_ACTION * MILLISECONDS_IN_SECONDS);

        return result;
    }

    @Override
    public void cancel(ResultTime result, Player player) {
        result.getTimer().cancel();
        if (result.isDone()) {
            final long startTime = result.getStartTime();
            final long endTime = System.currentTimeMillis();
            final long calculatedTime = (endTime - startTime) / MILLISECONDS_IN_SECONDS;
            final long timeBank = player.getTimeBank() - calculatedTime;
            player.setTimeBank(
                    timeBank < 0 ? 0 : timeBank
            );
        }
    }

    private void fillResult(ResultTime result) {
        final long startTime = System.currentTimeMillis();
        result.setDone(true);
        result.setStartTime(startTime);
    }

}
