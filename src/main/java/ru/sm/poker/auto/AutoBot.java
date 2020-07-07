package ru.sm.poker.auto;

import org.springframework.stereotype.Service;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.action.holdem.Fold;
import ru.sm.poker.action.holdem.Raise;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;

import java.util.Random;

@Service
public class AutoBot {

    private final Random random = new Random();

    public void auto(Player bot, HoldemRoundSettings holdemRoundSettings) {
        System.out.println("AUTO by: " + bot.getName());
        if (holdemRoundSettings.lastBetIsZero()) {
            bot.setAction(getRandomRaise(holdemRoundSettings));
            return;
        }

        final int i = random.nextInt(100);
        if (holdemRoundSettings.getLastBet() > holdemRoundSettings.getBigBlindBet()) {
            if (i <= 70) {
                bot.setAction(new Call(holdemRoundSettings.getLastBet()));
            } else {
                bot.setAction(new Fold());
            }
        }


    }

    private Action getRandomRaise(HoldemRoundSettings holdemRoundSettings) {
        return new Raise(holdemRoundSettings.getBigBlindBet() * 2);
    }
}
