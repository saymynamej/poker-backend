package ru.sm.poker.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.holdem.Fold;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.enums.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(of = "name")
@SuperBuilder
public class Player {

    private String name;

    private List<CardType> cards;

    private Long id;

    private Game game;

    @Builder.Default
    private RoleType roleType = RoleType.ORDINARY;

    private long chipsCount;

    @Setter
    private long chipsId;

    @Setter
    @Builder.Default
    private StateType stateType = StateType.IN_GAME;
    @Setter
    @Builder.Default
    private long timeBank = 60L;
    @Setter
    @Builder.Default
    private Action action = new Wait();

    @Setter
    @Getter
    @Builder.Default
    private boolean active = false;

    @Getter
    @Setter
    private String gameName;

    public Player(
            String name,
            List<CardType> cards,
            RoleType roleType,
            long chipsCount,
            StateType stateType,
            long timeBank,
            Action action,
            boolean active,
            String gameName
    ) {
        this.name = name;
        this.cards = cards;
        this.roleType = roleType;
        this.chipsCount = chipsCount;
        this.stateType = stateType;
        this.timeBank = timeBank;
        this.action = action;
        this.active = active;
        this.gameName = gameName;
    }

    public Player(String name, long chipsCount) {
        this.name = name;
        this.chipsCount = chipsCount;
    }

    public boolean isPlayer() {
        return roleType == RoleType.ORDINARY;
    }

    public boolean isButton() {
        return roleType == RoleType.BUTTON;
    }

    public boolean isBigBlind() {
        return roleType == RoleType.BIG_BLIND;
    }

    public boolean isSmallBlind() {
        return roleType == RoleType.SMALL_BLIND;
    }

    public void setButton() {
        this.roleType = RoleType.BUTTON;
    }

    public void setRole(RoleType roleType) {
        this.roleType = roleType;
    }

    public void removeRole() {
        this.roleType = RoleType.ORDINARY;
    }

    public void addChips(long chips) {
        this.chipsCount += chips;
    }

    public void setChipsCount(long chipsCount) {
        this.chipsCount = chipsCount;
    }

    public boolean hasGame() {
        return this.gameName != null;
    }

    public void removeChips(long chips) {
        if (chips > this.chipsCount) {
            this.chipsCount = 0;
            return;
        }
        this.chipsCount -= chips;
    }

    public void setWait() {
        this.action = new Wait();
    }

    public void changeState() {
        if (getStateType() == StateType.IN_GAME) {
            setStateType(StateType.AFK);
            setAction(new Fold());
            return;
        }
        setStateType(StateType.IN_GAME);
        setAction(new Wait());
    }

    public boolean isNotFirstMoveOnBigBlind() {
        return isBigBlind() && action.getActionType() != ActionType.WAIT;
    }

    public boolean hasZeroChips() {
        return getChipsCount() == 0;
    }

    public boolean hasChipsForAction(CountAction countAction) {
        return getChipsCount() >= countAction.getCount();
    }

    public boolean hasNotChipsForAction(CountAction countAction) {
        return !hasChipsForAction(countAction);
    }

    public boolean didAction() {
        return getAction().getActionType() != ActionType.WAIT;
    }

    public boolean isFolded() {
        return getAction().getActionType() == ActionType.FOLD;
    }

    public boolean isNotFolded() {
        return !isFolded();
    }


    public void setInActive() {
        setAction(new Fold());
        setStateType(StateType.AFK);
        setTimeBank(0L);
    }

    public void setActive() {
        setAction(new Wait());
        setStateType(StateType.IN_GAME);
        setTimeBank(60L);
    }

    public boolean isInAllIn() {
        return action.getActionType() == ActionType.ALLIN;
    }

    public boolean hasNotSpecialRoles() {
        return !isSmallBlind() && !isBigBlind() && !isButton();
    }

    public boolean isNotInGame() {
        return getStateType() == null || getStateType() == StateType.AFK || getStateType() == StateType.LEAVE;
    }

    public void addCards(List<CardType> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public boolean isBot() {
        return getPlayerType() == PlayerType.BOT;
    }

    public boolean isOrdinaryPlayer() {
        return getPlayerType() == PlayerType.ORDINARY;
    }

    public PlayerType getPlayerType() {
        return PlayerType.ORDINARY;
    }

    public Player copy() {
        return Player
                .builder()
                .name(name)
                .action(action)
                .active(active)
                .cards(cards)
                .chipsCount(chipsCount)
                .gameName(gameName)
                .timeBank(timeBank)
                .roleType(roleType)
                .stateType(stateType)
                .build();
    }
}