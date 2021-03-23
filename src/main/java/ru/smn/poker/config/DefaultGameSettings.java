package ru.smn.poker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.smn.poker.game.Table;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DefaultGameSettings {

    @Bean
    public Map<String, Table> mapForGames(){
        return new ConcurrentHashMap<>();
    }
}
