package ru.smn.poker.auto;

import org.springframework.stereotype.Service;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.holdem.Call;
import ru.smn.poker.action.holdem.Fold;
import ru.smn.poker.action.holdem.Raise;
import ru.smn.poker.dto.Player;
import ru.smn.poker.game.RoundSettings;

import java.util.Random;

@Service
public class AutoBot {

    private final Random random = new Random();

    public void auto(Player bot, RoundSettings roundSettings) {
        System.out.println("AUTO by: " + bot.getName());
        if (roundSettings.lastBetIsZero()) {
            bot.setAction(getRandomRaise(roundSettings));
            return;
        }

        final int i = random.nextInt(100);
        if (roundSettings.getLastBet() > roundSettings.getBigBlindBet()) {
            if (i <= 70) {
                bot.setAction(new Call(roundSettings.getLastBet()));
            } else {
                bot.setAction(new Fold());
            }
        }


    }

    private Action getRandomRaise(RoundSettings roundSettings) {
        return new Raise(roundSettings.getBigBlindBet() * 2);
    }
}
