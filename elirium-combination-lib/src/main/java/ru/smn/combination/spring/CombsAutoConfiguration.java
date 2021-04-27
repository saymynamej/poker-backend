package ru.smn.combination.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.smn.combination.ClassicCombinationService;
import ru.smn.combination.CombinationService;

@Configuration
public class CombsAutoConfiguration {

    @Bean
    public CombinationService combinationService() {
        return new ClassicCombinationService();
    }
}
