package ru.sm.poker.config.game;

import ru.sm.poker.enums.GameType;

public interface GameSettings {
    int getMaxPlayerSize();

    String getGameName();

    int getMinActivePlayers();

    int getMinPlayersForStart();

    long getStartSmallBlindBet();

    long getStartBigBlindBet();

    GameType getGameType();
}
