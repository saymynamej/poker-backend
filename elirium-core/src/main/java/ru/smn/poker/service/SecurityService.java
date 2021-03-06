package ru.smn.poker.service;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;

import java.util.List;

public interface SecurityService {

    boolean isLegalPlayer(String gameName, PlayerEntity player);

    TableSettings secureCards(List<String> filter, TableSettings tableSettings);
}
