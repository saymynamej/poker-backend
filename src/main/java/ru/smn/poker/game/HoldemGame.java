package ru.smn.poker.game;

import ru.smn.poker.config.game.GameSettings;

public class HoldemGame extends Game {

    public HoldemGame(GameSettings gameSettings, Table table) {
        super(gameSettings, table);
    }

    @Override
    public void start() {
        getRound().start();
    }

    @Override
    public TableSettings getRoundSettings() {
        return getRound().getTableSettings();
    }
}
