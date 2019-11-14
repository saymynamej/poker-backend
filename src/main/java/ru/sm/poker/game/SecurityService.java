package ru.sm.poker.game;

import ru.sm.poker.model.Player;

public interface SecurityService {

    boolean isLegalPlayer(String name, Player player);
}
