package ru.smn.poker.service;

import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;

import java.util.List;

public interface SecurityService {

    boolean isLegalPlayer(String gameName, Player player);

    boolean isLegalPlayer(Player player);

    HoldemRoundSettings secureCards(List<String> filter, HoldemRoundSettings holdemRoundSettings);
}
