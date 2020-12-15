package ru.smn.poker.service;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;

import java.util.List;

public interface SecurityService {

    boolean isLegalPlayer(String gameName, PlayerEntity player);

    boolean isLegalPlayer(PlayerEntity player);

    RoundSettings secureCards(List<String> filter, RoundSettings roundSettings);
}
