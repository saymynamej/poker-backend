package ru.sm.poker.game;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.Player;

import java.util.List;

public interface Round {

    void startRound();

    String getGameName();

    void reloadRound();

    HoldemRoundSettingsDTO getHoldemRoundSettingsDTO();

    List<Player> getPlayers();

}
