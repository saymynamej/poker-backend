package ru.smn.poker.config.game;

import ru.smn.poker.enums.GameType;

public interface GameSettings {
    int getMaxPlayerSize();

    int getMinActivePlayers();

    int getMinPlayersForStart();

    long getStartSmallBlindBet();

    long getStartBigBlindBet();

    GameType getGameType();

    Long getTableId();

    String getTableName();

    void setTableName(String gameName);

    void setTableId(Long gameId);


}
