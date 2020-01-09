package ru.sm.poker.util;

import com.github.javafaker.Faker;

import java.util.UUID;

public class GameUtil {

    private final static Faker faker = new Faker();

    public static String getRandomGameName() {
        return UUID.randomUUID().toString();
    }

    public static String getRandomGameNameFromJavaFaker() {
        return faker.funnyName().name();
    }
}
