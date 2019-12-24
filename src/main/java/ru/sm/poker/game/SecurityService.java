package ru.sm.poker.game;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;

public interface SecurityService {
    boolean isLegalPlayer(String name, Player player);

    boolean isLegalPlayer(Player player);

    RoundSettingsDTO secureCards(String name, RoundSettingsDTO roundSettingsDTO);
}
