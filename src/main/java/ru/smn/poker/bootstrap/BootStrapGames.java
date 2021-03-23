package ru.smn.poker.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.service.common.SimpleGameManagementService;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class BootStrapGames {
    private final SimpleGameManagementService simpleGameManagementService;

    @PostConstruct
    public void init2() {
        simpleGameManagementService.createTable(2, GameType.HOLDEM_HU);
    }

}
