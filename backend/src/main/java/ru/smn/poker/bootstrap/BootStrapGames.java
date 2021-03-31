package ru.smn.poker.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.service.common.SimpleGameTableCreator;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class BootStrapGames {
    private final SimpleGameTableCreator gameTableCreator;

    @PostConstruct
    public void init2() {
        gameTableCreator.createTable(2, GameType.HOLDEM_HU);
    }

}
