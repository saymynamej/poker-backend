package ru.sm.poker.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GameType {
    HOLDEM_FULL("HOLDEM_FULL"),
    HOLDEM_HU("HOLDEM_HU");

    private final String gameName;

    public static GameType getGameTypeByName(String name){
        if (name.equalsIgnoreCase(HOLDEM_FULL.gameName)){
            return HOLDEM_FULL;
        }
        return HOLDEM_HU;
    }

}

