package ru.smn.poker.auto;

import org.springframework.stereotype.Service;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.holdem.Call;
import ru.smn.poker.action.holdem.Fold;
import ru.smn.poker.action.holdem.Raise;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;

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
