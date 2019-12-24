package ru.sm.poker.game;

import ru.sm.poker.model.Player;
import ru.sm.poker.dto.RoundSettingsDTO;

public interface Game {

    String getName();

    void start();

    void stop();

    void reload();

    RoundSettingsDTO getRoundSettings();

    int getMaxPlayersSize();

    void addPlayer(Player player);

    void removePlayer(Player player);
}
