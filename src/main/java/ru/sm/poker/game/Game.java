package ru.sm.poker.game;

import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;

import java.util.List;

@Slf4j
public abstract class Game {

    private final String gameName;
    private final int maxPlayerSize;
    private final List<Player> players;
    private final Round round;
    protected boolean isStarted = true;

    public Game(String gameName, int maxPlayersSize, List<Player> players, Round round){
        this.gameName = gameName;
        this.maxPlayerSize = maxPlayersSize;
        this.players = players;
        this.round = round;
    }


    protected Round getRound(){
        return this.round;
    }

    protected List<Player> getPlayers(){
        return this.players;
    }
    public boolean isStarted(){
        return isStarted;
    }

    public String getName(){
        return this.gameName;
    }

    public int getMaxPlayersSize(){
        return this.maxPlayerSize;
    }

    public void addPlayer(Player player){
        if (!this.players.contains(player)) {
            this.players.add(player);
            return;
        }
        log.info(player + " already in game");
    }

    public void removePlayer(Player player){
        if (!this.players.contains(player)){
            log.info(player + " :player not found");
            return;
        }
        this.players.remove(player);
    }


    public abstract void start();

    public abstract void stop();

    public abstract void reload();

    public abstract RoundSettingsDTO getRoundSettings();


}
