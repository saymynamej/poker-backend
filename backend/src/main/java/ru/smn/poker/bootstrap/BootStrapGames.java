package ru.smn.poker.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.service.GameTableCreator;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class BootStrapGames {
    private final GameTableCreator gameTableCreator;

    @PostConstruct
    public void init2() {
        gameTableCreator.create(2, GameType.HOLDEM_HU);
    }

//    @PostConstruct
//    public void init() {
//        gameTableCreator.restoreAll();
//    }

}
