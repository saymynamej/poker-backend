package ru.smn.poker.game;

import ru.smn.poker.entities.PlayerEntity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HoldemRoundSettingsManagerFactory {
    private final static Random random = new SecureRandom();

    public static RoundSettingsManager getRoundSettingsManager(
            List<PlayerEntity> players,
            String gameName,
            long bigBlindBet,
            long smallBlindBet,
            long gameId
    ) {
        return players.size() > 2
                ? new HoldemRoundSettingsManager(random, new ArrayList<>(players), gameName, bigBlindBet, smallBlindBet, gameId)
                : new HoldemRoundSettingsManagerHU(random, new ArrayList<>(players), gameName, bigBlindBet, smallBlindBet, gameId);
    }
}
