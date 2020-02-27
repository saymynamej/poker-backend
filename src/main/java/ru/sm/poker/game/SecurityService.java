package ru.sm.poker.game;

import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;

import java.util.List;

public interface SecurityService {

    boolean isLegalPlayer(String gameName, Player player);

    boolean isLegalPlayer(Player player);

    HoldemRoundSettings secureCards(List<String> filter, HoldemRoundSettings holdemRoundSettings);
}
