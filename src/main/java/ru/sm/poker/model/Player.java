package ru.sm.poker.model;

import lombok.*;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.holdem.Wait;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(of = "name")
public class Player {

    private String name;
    private List<CardType> cards;
    private RoleType roleType = RoleType.PLAYER;
    private long chipsCount;
    @Setter
    private StateType stateType = StateType.IN_GAME;
    @Setter
    private long timeBank = 60L;
    @Setter
    private Action action = new Wait();

    @Setter
    @Getter
    private boolean active = false;

    @Getter
    @Setter
    private String gameName;

    @Builder
    public Player(String name, List<CardType> cards, RoleType roleType, long chipsCount, StateType stateType, long timeBank, Action action, boolean active, String gameName) {
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
        this.roleType = RoleType.PLAYER;
    }

    public void addChips(long chips) {
        this.chipsCount += chips;
    }

    public void setChipsCount(long chipsCount){
        this.chipsCount = chipsCount;
    }
    public void removeChips(long chips) {
        if (chips > this.chipsCount) {
            this.chipsCount = 0;
            return;
        }
        this.chipsCount -= chips;
    }

    public void addCards(List<CardType> cards) {
        this.cards = new ArrayList<>(cards);
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
