package ru.sm.poker.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GameType {
    HOLDEM("HOLDEM");

    private final String gameName;

}

