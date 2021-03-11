package ru.smn.poker.service;

import ru.smn.poker.game.TableSettings;

public interface OrderService {
    boolean start(TableSettings tableSettings);
}
