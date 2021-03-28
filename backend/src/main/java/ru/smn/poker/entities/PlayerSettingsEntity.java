package ru.smn.poker.entities;


import lombok.*;
import ru.smn.poker.action.Action;
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
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player_settings")
@EqualsAndHashCode()
@ToString
public class PlayerSettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private List<ActionEntity> actions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "settings", fetch = FetchType.EAGER)
    private List<CardEntity> cards;

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

    private String tableName;

    @Transient
    private Action action;

    public void removeChips(long chips) {
        if (chips > getChipsCount().getCount()) {
            getChipsCount().setCount(0L);
            return;
        }
        getChipsCount().setCount(getChipsCount().getCount() - chips);
    }

    public boolean isPlayer() {
        return this.getRoleType() == RoleType.ORDINARY;
    }

    public boolean isButton() {
        return this.getRoleType() == RoleType.BUTTON;
    }

    public boolean isBigBlind() {
        return this.getRoleType() == RoleType.BIG_BLIND;
    }

    public boolean isSmallBlind() {
        return this.getRoleType() == RoleType.SMALL_BLIND;
    }

    public void setButton() {
        setRoleType(RoleType.BUTTON);
    }

    public void setRole(RoleType roleType) {
        setRoleType(roleType);
    }

    public void removeRole() {
        setRoleType(RoleType.ORDINARY);
    }

    public void addChips(long chips) {
        if (getChipsCount() == null) {
            setChipsCount(new ChipsCountEntity());
        }
        getChipsCount().setCount(getChipsCount().getCount() + chips);
    }

    public void setWait() {
        action = new Wait();
    }

    public boolean isNotFirstMoveOnBigBlind() {
        return isBigBlind() && getAction().getActionType() != ActionType.WAIT;
    }

    public boolean hasChipsForAction(Action action) {
        return getChipsCount().getCount() >= action.getCount();
    }

    public boolean hasNotChipsForAction(Action action) {
        return !hasChipsForAction(action);
    }

    public boolean didAction() {
        return getAction().getActionType() != ActionType.WAIT;
    }

    public boolean isFolded() {
        return getAction().getActionType() == ActionType.FOLD;
    }

    public void setInActive() {
        setAction(new Fold());
        setStateType(StateType.AFK);
        setTimeBank(0L);
    }

    public boolean hasNotSpecialRoles() {
        return !isSmallBlind() && !isBigBlind() && !isButton();
    }

    public boolean isNotInGame() {
        final StateType stateType = getStateType();
        return stateType == null || stateType == StateType.AFK || stateType == StateType.LEAVE;
    }

    public void addCards(List<CardEntity> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public PlayerType getPlayerType() {
        return PlayerType.ORDINARY;
    }
}
