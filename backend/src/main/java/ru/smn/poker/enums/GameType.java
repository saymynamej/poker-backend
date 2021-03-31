package ru.smn.poker.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.smn.poker.game.Table;

@RequiredArgsConstructor
@Getter
public enum GameType {
    HOLDEM_FULL("HOLDEM_FULL"),
    HOLDEM_HU("HOLDEM_HU");

    private final String gameName;

}

