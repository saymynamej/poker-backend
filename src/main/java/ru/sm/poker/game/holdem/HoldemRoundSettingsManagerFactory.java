package ru.sm.poker.game.holdem;

import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.game.RoundSettingsManager;

import java.util.ArrayList;
import java.util.List;

public class HoldemRoundSettingsManagerFactory {

    public static RoundSettingsManager getRoundSettingsManager(
            List<PlayerDTO> players,
            String gameName,
            long bigBlindBet,
            long smallBlindBet
    ) {
        return players.size() > 2
                ? new HoldemRoundSettingsManager(new ArrayList<>(players), gameName, bigBlindBet, smallBlindBet)
                : new HoldemRoundSettingsManagerHU(new ArrayList<>(players), gameName, bigBlindBet, smallBlindBet);
    }
}
