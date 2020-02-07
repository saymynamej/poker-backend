package ru.sm.poker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;


@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "config.socket")
public class SocketMappingConfig {
    @NotNull
    private String readyPlayersPath;

    @NotNull
    private String pokerGamePath;

    @NotNull
    private String errorPath;

    @NotNull
    private String flopPath;

    @NotNull
    private String ternPath;

    @NotNull
    private String riverPath;

    @NotNull
    private String bankPath;

    @NotNull
    private String gamesPath;

}
