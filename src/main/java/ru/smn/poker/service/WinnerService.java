package ru.smn.poker.service;

import ru.smn.poker.dto.PlayerCombination;
import ru.smn.poker.game.RoundSettings;

import java.util.List;

public interface WinnerService {
    List<PlayerCombination> findWinners(RoundSettings roundSettings);
}
