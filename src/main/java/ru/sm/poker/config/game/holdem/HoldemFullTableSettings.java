package ru.sm.poker.config.game.holdem;

import lombok.RequiredArgsConstructor;
import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.enums.GameType;

@RequiredArgsConstructor
public class HoldemFullTableSettings implements GameSettings {

    private final static int MAX_PLAYERS_SIZE = 9;

    private final static int MIN_ACTIVE_PLAYERS = 3;

    private final static int MIN_PLAYERS_FOR_START = 3;

    private final static int MIN_SMALL_BLIND_BET = 1;

    private final static int MIN_BIG_BLIND_BET = 2;

    private final String gameName;

    private final GameType gameType;


    @Override
    public String getGameName() {
        return gameName;
    }

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
