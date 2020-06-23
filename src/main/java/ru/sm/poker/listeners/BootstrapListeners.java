package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class BootstrapListeners {
    private final List<GameListener> listeners;

    @PostConstruct
    public void init() {
        listeners.forEach(GameListener::listen);
    }

}
