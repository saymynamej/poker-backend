package ru.smn.poker.util;

import com.github.javafaker.Faker;

import java.util.HashSet;
import java.util.Set;

public class GameUtil {

    private static final Set<String> ALREADY_USER = new HashSet<>();

    private final static Faker faker = new Faker();

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
