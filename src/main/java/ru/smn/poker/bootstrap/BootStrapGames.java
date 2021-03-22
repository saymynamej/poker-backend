package ru.smn.poker.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.service.GameManagementService;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class BootStrapGames {
    private final GameManagementService gameManagementService;

    @PostConstruct
    public void init2() {
        gameManagementService.create(2, 5000L, GameType.HOLDEM_HU);
    }

}
