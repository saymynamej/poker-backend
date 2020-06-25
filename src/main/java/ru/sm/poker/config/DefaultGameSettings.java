package ru.sm.poker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sm.poker.game.Game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DefaultGameSettings {

    @Bean
    public Map<String, Game> mapForGames(){
        return new ConcurrentHashMap<>();
    }
}
