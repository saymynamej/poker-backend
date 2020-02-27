package ru.sm.poker.auto;

import org.springframework.stereotype.Service;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.holdem.Raise;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.util.ThreadUtil;

@Service
public class HoldemAutoBot {

    public void doRandomAction(Player player, HoldemRoundSettings holdemRoundSettings) {
        ThreadUtil.sleep(5);
        player.setAction(getAction(holdemRoundSettings));
    }

    public Action getAction(HoldemRoundSettings holdemRoundSettings) {
        return new Raise(10);
    }
}
