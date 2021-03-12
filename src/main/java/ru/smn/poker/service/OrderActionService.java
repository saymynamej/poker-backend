package ru.smn.poker.service;

import ru.smn.poker.game.TableSettings;

public interface OrderActionService {
    boolean start(TableSettings tableSettings);
}
