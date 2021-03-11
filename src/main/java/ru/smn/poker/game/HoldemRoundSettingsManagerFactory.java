package ru.smn.poker.game;

import ru.smn.poker.entities.PlayerEntity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HoldemRoundSettingsManagerFactory {

    private final static Random random = new SecureRandom();

    public static TableSettingsManager getManager(
            List<PlayerEntity> players,
            String gameName,
            long bigBlindBet,
            long smallBlindBet,
            long gameId
    ) {
        return new HoldemTableSettingsManagerHU(random, new ArrayList<>(players), gameName, bigBlindBet, smallBlindBet, gameId);

    }

    public static TableSettingsManager getManager(
            TableSettings tableSettings
    ) {
        return tableSettings.getPlayers().size() > 2
                ? new HoldemTableSettingsManager(random, tableSettings)
                : new HoldemTableSettingsManagerHU(random, tableSettings);
    }
}
