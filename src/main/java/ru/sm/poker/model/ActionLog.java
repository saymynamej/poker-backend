package ru.sm.poker.model;

import lombok.RequiredArgsConstructor;
import ru.sm.poker.action.Action;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ActionLog {
    private final Action action;
    private final LocalDateTime date;
    private final Player player;
}
