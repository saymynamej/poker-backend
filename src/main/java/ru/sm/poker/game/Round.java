package ru.sm.poker.game;

import ru.sm.poker.model.Player;

import java.util.List;

public abstract class Round {
    private final List<Player> players;

    public Round(List<Player> players){
        this.players = players;
    }

    protected abstract void startRound();

    protected List<Player> getPlayers(){
        return this.players;
    }

    public abstract Player getActivePlayer();
}