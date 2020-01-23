package ru.sm.poker.base;

import ru.sm.poker.model.Player;

public class PlayerSettings {

    public static Player getDefaultPlayerForHoldem(String playerName) {
        return Player.builder()
                .name(playerName)
                .timeBank(60L)
                .chipsCount(5000)
                .build();
    }
}
