package ru.smn.poker.util;

public class LongUtil {

    public static long parseLong(String number) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            throw new RuntimeException("cannot parse  - " + number);
        }
    }
}
