package ru.smn.poker.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.smn.poker.service.GameTableCreator;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
@Profile(value = {"!test"})
public class BootStrapGames {
    private final GameTableCreator gameTableCreator;

    @PostConstruct
    public void init() {
        gameTableCreator.restoreAll();
    }

}
