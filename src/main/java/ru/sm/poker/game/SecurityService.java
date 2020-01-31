package ru.sm.poker.game;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;

import java.util.List;

public interface SecurityService {
    boolean isLegalPlayer(String gameName, PlayerDTO playerDTO);

    boolean isLegalPlayer(PlayerDTO playerDTO);

    HoldemRoundSettingsDTO secureCards(List<String> filter, HoldemRoundSettingsDTO holdemRoundSettingsDTO);
}
