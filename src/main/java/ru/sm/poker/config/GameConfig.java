package ru.sm.poker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class GameConfig {

    @Bean
    public Map<String, Game> getGames(){
        return new HashMap<>();
    }

    @Bean
    public List<Player> getPlayers(){
        return new ArrayList<>();
    }
}
