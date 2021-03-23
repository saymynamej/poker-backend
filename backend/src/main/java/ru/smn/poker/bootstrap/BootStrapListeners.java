package ru.smn.poker.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.smn.poker.listeners.GameListener;

import javax.annotation.PostConstruct;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class BootStrapListeners {
    private final List<GameListener> listeners;

    @PostConstruct
    public void init() {
        listeners.forEach(GameListener::listen);
    }

}
