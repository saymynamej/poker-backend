package ru.sm.poker.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.PlayerType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.action.Action;
import ru.sm.poker.model.action.Wait;

@Getter
@ToString
@EqualsAndHashCode(of = "name")
public class Player {
    private String name;
    private CardType[] cardTypes = new CardType[2];
    private PlayerType playerType = PlayerType.PLAYER;
    private long chipsCount;
    @Setter
    private StateType stateType = StateType.IN_GAME;
    @Setter
    private int timeBank = 60;
    @Setter
    private Action action = new Wait();

    public Player(String name, long chipsCount) {
        this.name = name;
        this.chipsCount = chipsCount;
    }

    public boolean isButton() {
        return playerType == PlayerType.BUTTON;
    }

    public boolean isBigBlind() {
        return playerType == PlayerType.BIG_BLIND;
    }

    public boolean isSmallBlind() {
        return playerType == PlayerType.SMALL_BLIND;
    }

    public void setButton() {
        this.playerType = PlayerType.BUTTON;
    }

    public void setRole(PlayerType playerType) {
        this.playerType = playerType;
    }

    public void removeRole() {
        this.playerType = PlayerType.PLAYER;
    }

    public void addChips(long chips) {
        this.chipsCount += chips;
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
