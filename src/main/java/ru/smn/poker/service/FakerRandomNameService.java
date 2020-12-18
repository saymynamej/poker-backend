package ru.smn.poker.service;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class FakerRandomNameService implements RandomNameService {
    private static final Set<String> ALREADY_USER = new HashSet<>();
    private final static Faker faker = new Faker();

    @Override
    public String getRandomName() {
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
