package ru.smn.poker.game.holdem;

import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettingsManager;

import java.util.ArrayList;
import java.util.List;

public class HoldemRoundSettingsManagerFactory {

    public static RoundSettingsManager getRoundSettingsManager(
            List<PlayerEntity> players,
            String gameName,
            long bigBlindBet,
            long smallBlindBet,
            long gameId
    ) {
        return players.size() > 2
                ? new HoldemRoundSettingsManager(new ArrayList<>(players), gameName, bigBlindBet, smallBlindBet, gameId)
                : new HoldemRoundSettingsManagerHU(new ArrayList<>(players), gameName, bigBlindBet, smallBlindBet, gameId);
    }
}
