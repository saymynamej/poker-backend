package ru.sm.poker.util;

import java.util.UUID;

public class GameUtil {
    public static String getRandomGameName() {
        return UUID.randomUUID().toString();
    }
}
