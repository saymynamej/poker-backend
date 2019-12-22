package ru.sm.poker.model;

import lombok.*;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.action.Action;
import ru.sm.poker.model.action.Wait;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@EqualsAndHashCode(of = "name")

public class Player {

    private String name;
    private CardType[] cardTypes = new CardType[2];
    private RoleType roleType = RoleType.PLAYER;
    private long chipsCount;
    @Setter
    private StateType stateType = StateType.IN_GAME;
    @Setter
    private int timeBank = 60;
    @Setter
    private Action action = new Wait("null");

    @Setter
    @Getter
    private boolean active = false;

    @Getter
    @Setter
    private String gameName;


    @Builder
    public Player(String name, CardType[] cardTypes, RoleType roleType, long chipsCount, StateType stateType, int timeBank, Action action, boolean active, String gameName) {
        this.name = name;
        this.cardTypes = cardTypes;
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

    public List<CardType> getCardTypesAsList(){
        return Arrays.stream(cardTypes).collect(Collectors.toList());
    }

    public void removeChips(long chips) {
        if (chips > this.chipsCount) {
            this.chipsCount = 0;
            return;
        }
        this.chipsCount -= chips;
    }

    public void addCards(CardType[] cardTypes) {
        if (cardTypes.length > 2) {
            throw new RuntimeException("size of card type array must be2");
        }
        this.cardTypes[0] = cardTypes[0];
        this.cardTypes[1] = cardTypes[1];
    }
}
