package ru.sm.poker.util;

import com.github.javafaker.Faker;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GameUtil {

    private static final Set<String> ALREADY_USER = new HashSet<>();

    private final static Faker faker = new Faker();

    public static String getRandomGameName() {
        return UUID.randomUUID().toString();
    }

    public static String getRandomGOTCityName() {
        String city = faker.gameOfThrones().city();
        if (ALREADY_USER.contains(city)) {
            while (ALREADY_USER.contains(city)) {
                city = faker.gameOfThrones().city();
            }
        }
        ALREADY_USER.add(city);
        return city;
    }
}
