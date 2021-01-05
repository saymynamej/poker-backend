package ru.smn.poker.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.action.holdem.Fold;
import ru.smn.poker.action.holdem.Wait;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.enums.PlayerType;
import ru.smn.poker.enums.RoleType;
import ru.smn.poker.enums.StateType;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(of = "name")
@Builder
@AllArgsConstructor
public class Player {

    private String name;

    @Setter
    private List<Card> cards;

    private Long id;

    private Game game;

    private RoleType roleType;

    private long chipsCount;

    @Setter
    private long chipsId;

    @Setter
    private StateType stateType;
    @Setter
    private long timeBank;
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
            List<Card> cards,
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

    public void addCards(List<Card> cards) {
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
