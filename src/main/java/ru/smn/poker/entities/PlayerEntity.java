package ru.smn.poker.entities;

import lombok.*;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.action.holdem.Fold;
import ru.smn.poker.action.holdem.Wait;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.enums.PlayerType;
import ru.smn.poker.enums.RoleType;
import ru.smn.poker.enums.StateType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "players")
@EqualsAndHashCode(of = "name")
@ToString
public class PlayerEntity {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ActionEntity> actions;

    @Transient
    private Action action;

    private String name;

    private String password;


    private String gameName;


    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CardEntity> cards;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private GameEntity game;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private RoleType roleType = RoleType.ORDINARY;

    @Enumerated(value = EnumType.STRING)
    private PlayerType playerType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "chips_id")
    private ChipsCountEntity chipsCount;

    @Enumerated(value = EnumType.STRING)
    private StateType stateType;

    private long timeBank;

    private boolean active;

    private boolean enable;


    public void removeChips(long chips) {
        if (chips > this.chipsCount.getCount()) {
            this.chipsCount.setCount(0L);
            return;
        }
        this.chipsCount.setCount(this.chipsCount.getCount() - chips);
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
        if (chipsCount == null){
            chipsCount = new ChipsCountEntity();
        }
        this.chipsCount.setCount(this.chipsCount.getCount() + chips);
    }

    public void setChipsCount(ChipsCountEntity chipsCount) {
        this.chipsCount = chipsCount;
    }

    public boolean hasGame() {
        return this.game != null;
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
        return getChipsCount().getCount() == 0;
    }

    public boolean hasChipsForAction(CountAction countAction) {
        return getChipsCount().getCount() >= countAction.getCount();
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

    public void addCards(List<CardEntity> cards) {
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

    public PlayerEntity copy() {
        return PlayerEntity
                .builder()
                .name(name)
                .action(action)
                .active(active)
                .cards(cards)
                .chipsCount(chipsCount)
                .game(game)
                .timeBank(timeBank)
                .roleType(roleType)
                .stateType(stateType)
                .build();
    }

}
