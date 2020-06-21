package ru.sm.poker.config.game.holdem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.enums.GameType;

@RequiredArgsConstructor
@Component
public class HoldemHUTableSettings implements GameSettings {

    private final static int MAX_PLAYERS_SIZE = 2;

    private final static int MIN_ACTIVE_PLAYERS = 2;

    private final static int MIN_PLAYERS_FOR_START = 2;

    private final static int MIN_SMALL_BLIND_BET = 1;

    private final static int MIN_BIG_BLIND_BET = 2;

    private final GameType gameType = GameType.HOLDEM_HU;

    @Override
    public int getMaxPlayerSize() {
        return MAX_PLAYERS_SIZE;
    }

    @Override
    public int getMinActivePlayers() {
        return MIN_ACTIVE_PLAYERS;
    }

    @Override
    public int getMinPlayersForStart() {
        return MIN_PLAYERS_FOR_START;
    }

    @Override
    public long getStartSmallBlindBet() {
        return MIN_SMALL_BLIND_BET;
    }


    @Override
    public long getStartBigBlindBet() {
        return MIN_BIG_BLIND_BET;
    }

    @Override
    public GameType getGameType() {
        return gameType;
    }
}
