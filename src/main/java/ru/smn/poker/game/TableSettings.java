package ru.smn.poker.game;

import ru.smn.poker.action.Action;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.util.PlayerUtil;
import ru.smn.poker.stream.PlayerPredicates;

import java.util.List;
import java.util.Map;

public interface TableSettings {

    boolean isNotFirstMoveOnBigBlind();

    Map<PlayerEntity, List<Action>> getStageHistory();

    Map<PlayerEntity, List<Action>> getFullHistory();

    List<PlayerEntity> getPlayers();

    List<CardType> getFlop();

    CardType getTern();

    CardType getRiver();

    String getGameName();

    long getSmallBlindBet();

    long getGameId();

    long getBigBlindBet();

    PlayerEntity getButton();

    PlayerEntity getSmallBlind();

    PlayerEntity getBigBlind();

    StageType getStageType();

    Long getLastBet();

    long getBank();

    Long getHandId();

    void setHandId(Long id);

    PlayerEntity getActivePlayer();

    void setLastBet(Long lastBet);

    void setBank(long bank);

    boolean isFinished();

    void setFinished(boolean r);

    void setActivePlayer(PlayerEntity activePlayer);

    default boolean lastBetIsNotZero() {
        return getLastBet() != 0;
    }

    default boolean lastBetIsZero(){
        return !lastBetIsNotZero();
    }

    default boolean playersInAllIn() {
        return getPlayersInGame().stream()
                .allMatch(PlayerPredicates.playerInAllIn());
    }

    default boolean allPlayersCheck() {
        return getPlayersInGame()
                .stream()
                .filter(PlayerPredicates.playerInAllIn().negate())
                .allMatch(PlayerPredicates.playersHasCheck());
    }

    default List<PlayerEntity> getPlayersInGame(){
        return PlayerUtil.getPlayersInAction(getPlayers());
    }

    default boolean isOnePlayerLeft() {
        return getPlayersInGame().size() == 1;
    }

}
