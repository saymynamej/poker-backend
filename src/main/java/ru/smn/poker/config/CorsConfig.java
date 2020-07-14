package ru.smn.poker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "config.cors")
public class CorsConfig {
    private List<String> hosts;
    private boolean credentials;
    private List<String> methods;
    private List<String> headers;
}
