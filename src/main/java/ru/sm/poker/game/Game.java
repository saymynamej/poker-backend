package ru.sm.poker.game;

import ru.sm.poker.model.Player;
import ru.sm.poker.model.RoundSettings;

public interface Game {

    String getName();

    void start();

    void stop();

    void reload();

    RoundSettings getRoundSettings();

    int getMaxPlayersSize();

    void addPlayer(Player player);

    void removePlayer(Player player);
}
