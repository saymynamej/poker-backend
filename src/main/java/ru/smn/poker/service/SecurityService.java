package ru.smn.poker.service;

import ru.smn.poker.dto.Player;
import ru.smn.poker.game.RoundSettings;

import java.util.List;

public interface SecurityService {

    boolean isLegalPlayer(String gameName, Player player);

    boolean isLegalPlayer(Player player);

    RoundSettings secureCards(List<String> filter, RoundSettings roundSettings);
}
