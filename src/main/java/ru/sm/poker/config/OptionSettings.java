package ru.sm.poker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.Game;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Configuration
public class OptionSettings {
    public List<GameSettings> gameSettingsList;

    public OptionSettings(List<GameSettings> gameSettingsList) {
        this.gameSettingsList = gameSettingsList;
    }

    @Bean
    public Map<Game, ExecutorService> getMapForRunnableGames(){
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<GameType, GameSettings> getMapGameSettings(){
        return gameSettingsList.stream().collect(Collectors.toMap(
                GameSettings::getGameType,
                gameSettings -> gameSettings
        ));
    }

}
