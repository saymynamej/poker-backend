package ru.sm.poker.config.game;

public interface GameSettings {
    int getMaxPlayerSize();

    String getGameName();

    int getMinActivePlayers();

    int getMinPlayersForStart();

    long getStartSmallBlindBet();

    long getStartBigBlindBet();
}
