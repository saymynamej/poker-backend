package ru.smn.poker.game;

import ru.smn.combination.data.CardType;
import ru.smn.poker.action.Action;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.stream.PlayerPredicates;
import ru.smn.poker.util.PlayerUtil;

import java.util.List;
import java.util.Map;

public interface TableSettings {

    void setPlayers(List<PlayerEntity> players);

    boolean isNotFirstMoveOnBigBlind();

    void setFullHistory(Map<PlayerEntity, List<Action>> actions);

    void setStageHistory(Map<PlayerEntity, List<Action>> actions);

    Map<PlayerEntity, List<Action>> getStageHistory();

    Map<PlayerEntity, List<Action>> getFullHistory();

    List<PlayerEntity> getPlayers();

    List<CardType> getFlop();

    CardType getTern();

    CardType getRiver();

    String getTableName();

    long getSmallBlindBet();

    long getTableId();

    long getBigBlindBet();

    PlayerEntity getButton();

    PlayerEntity getSmallBlind();

    PlayerEntity getBigBlind();

    StageType getStageType();

    Long getLastBet();

    long getBank();

    Long getHandId();

    void setTableName(String name);

    PlayerEntity getActivePlayer();

    void setLastBet(Long lastBet);

    void setBank(long bank);

    boolean isFinished();

    void setFinished(boolean r);

    void setActivePlayer(PlayerEntity activePlayer);

    void setFlop(List<CardType> flop);

    void setTern(CardType tern);

    void setRiver(CardType river);

    void setSmallBlindBet(long smallBlindBet);

    void setTableId(long tableId);

    void setBigBlindBet(long bigBlindBet);

    void setButton(PlayerEntity button);

    void setSmallBlind(PlayerEntity smallBlind);

    void setBigBlind(PlayerEntity bigBlind);

    void setStageType(StageType stageType);

    void setHandId(Long handId);

    void setAfk(boolean isAfk);

    TableSettings copy();

    void reset();

    TableSettings copy(List<PlayerEntity> players);

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
