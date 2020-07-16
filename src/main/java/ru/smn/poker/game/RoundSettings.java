package ru.smn.poker.game;

import ru.smn.poker.action.Action;
import ru.smn.poker.dto.Player;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.util.PlayerUtil;
import ru.smn.poker.util.StreamUtil;

import java.util.List;
import java.util.Map;

public interface RoundSettings {

    Map<Player, List<Action>> getStageHistory();

    Map<Player, List<Action>> getFullHistory();

    List<Player> getPlayers();

    List<CardType> getFlop();

    CardType getTern();

    CardType getRiver();

    String getGameName();

    long getSmallBlindBet();

    long getGameId();

    long getBigBlindBet();

    Player getButton();

    Player getSmallBlind();

    Player getBigBlind();

    StageType getStageType();

    Long getLastBet();

    long getBank();

    Player getActivePlayer();

    void setLastBet(Long lastBet);

    void setBank(long bank);

    void setActivePlayer(Player activePlayer);

    default boolean lastBetIsNotZero() {
        return getLastBet() != 0;
    }

    default boolean lastBetIsZero(){
        return !lastBetIsNotZero();
    }

    default boolean playersInAllIn() {
        return getPlayersInGame()
                .stream()
                .allMatch(StreamUtil.playerInAllIn());
    }

    default boolean allPlayersCheck() {
        return getPlayersInGame()
                .stream()
                .filter(StreamUtil.playerInAllIn().negate())
                .allMatch(StreamUtil.playersHasCheck());
    }

    default List<Player> getPlayersInGame(){
        return PlayerUtil.getPlayersInGame(getPlayers());
    }

    default boolean isOnePlayerLeft() {
        return getPlayersInGame().size() == 1;
    }

}
