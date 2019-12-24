package ru.sm.poker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class GameConfig {

    @Bean
    public Map<String, Game> getGames() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Queue<Player> getPlayers() {
        return new LinkedBlockingQueue<>();
    }
}
