package ru.sm.poker.service;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;

import java.util.List;

public interface SecurityService {

    boolean isLegalPlayer(String gameName, PlayerDTO player);

    boolean isLegalPlayer(PlayerDTO player);

    HoldemRoundSettingsDTO secureCards(List<String> filter, HoldemRoundSettingsDTO holdemRoundSettings);
}
