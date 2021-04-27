package ru.smn.poker.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.service.GameTableCreator;

import javax.annotation.PostConstruct;

//@Profile(value = {"test"})
@RequiredArgsConstructor
@Service
public class TestTablesCreator {
    private final GameTableCreator gameTableCreator;

    @PostConstruct
    public void init() {
        gameTableCreator.create(2, GameType.HOLDEM_HU);
    }

}
