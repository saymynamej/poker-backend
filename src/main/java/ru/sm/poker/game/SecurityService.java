package ru.sm.poker.game;

import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.model.Player;

import java.util.List;

public interface SecurityService {
    boolean isLegalPlayer(String name, Player player);

    boolean isLegalPlayer(Player player);

    HoldemRoundSettingsDTO secureCards(List<String> filter, HoldemRoundSettingsDTO holdemRoundSettingsDTO);
}
