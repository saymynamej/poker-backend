package ru.sm.poker.game;

import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;

import java.util.List;

@Slf4j
public abstract class Game {

    private final GameSettings gameSettings;
    private final List<Player> players;
    private final Round round;
    protected boolean isEnable = true;

    public Game(GameSettings gameSettings, List<Player> players, Round round){
        this.gameSettings = gameSettings;
        this.players = players;
        this.round = round;
    }

    protected Round getRound(){
        return this.round;
    }

    protected List<Player> getPlayers(){
        return this.players;
    }

    public boolean isEnable(){
        return isEnable;
    }

    public String getName(){
        return this.gameSettings.getGameName();
    }

    public int getMaxPlayersSize(){
        return this.gameSettings.getMaxPlayerSize();
    }

    protected GameSettings getGameSettings(){
        return gameSettings;
    }

    public void addPlayer(Player player){
        if (!this.players.contains(player)) {
            this.players.add(player);
            return;
        }
    }

    public void removePlayer(Player player){
        this.players.remove(player);
    }


    public abstract void start();

    public abstract void enable();

    public abstract void disable();

    public abstract void reload();

    public abstract RoundSettingsDTO getRoundSettings();

}
