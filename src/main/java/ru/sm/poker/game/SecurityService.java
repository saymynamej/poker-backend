package ru.sm.poker.game;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;

import java.util.List;

public interface SecurityService {
    boolean isLegalPlayer(String name, Player player);

    boolean isLegalPlayer(Player player);

    RoundSettingsDTO secureCards(List<String> filter, RoundSettingsDTO roundSettingsDTO);
}
