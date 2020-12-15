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
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "settings_id")
    private PlayerSettingsEntity settings;

    private String name;

    private String password;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private RoleType roleType = RoleType.ORDINARY;

    private boolean enable;

    @Transient
    private Action action;


    public void removeChips(long chips) {
        if (chips > this.settings.getChipsCount().getCount()) {
            this.settings.getChipsCount().setCount(0L);
            return;
        }
        this.settings.getChipsCount().setCount(this.settings.getChipsCount().getCount() - chips);
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
        this.settings.setRoleType(RoleType.BUTTON);
    }

    public void setRole(RoleType roleType) {
        this.settings.setRoleType(roleType);
    }

    public void removeRole() {
        this.settings.setRoleType(RoleType.ORDINARY);
    }

    public void addChips(long chips) {
        if (this.settings.getChipsCount() == null){
            this.settings.setChipsCount(new ChipsCountEntity());
        }

        this.settings.getChipsCount().setCount(this.settings.getChipsCount().getCount() + chips);
    }

    public void setChipsCount(ChipsCountEntity chipsCount) {
        this.settings.setChipsCount(chipsCount);
    }

    public boolean hasGame() {
        return this.settings.getGame() != null;
    }

    public void setWait() {
        this.action = new Wait();
    }

    public void changeState() {
        if (this.settings.getStateType() == StateType.IN_GAME) {
            this.settings.setStateType(StateType.AFK);
            setAction(new Fold());
            return;
        }
        this.settings.setStateType(StateType.IN_GAME);
        setAction(new Wait());
    }

    public boolean isNotFirstMoveOnBigBlind() {
        return isBigBlind() && action.getActionType() != ActionType.WAIT;
    }

    public boolean hasChipsForAction(CountAction countAction) {
        return this.settings.getChipsCount().getCount() >= countAction.getCount();
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
        this.settings.setStateType(StateType.AFK);
        this.settings.setTimeBank(0L);

    }

    public void setActive() {
        setAction(new Wait());
        this.settings.setStateType(StateType.IN_GAME);
        this.settings.setTimeBank(60L);
    }

    public boolean isInAllIn() {
        return action.getActionType() == ActionType.ALLIN;
    }

    public boolean hasNotSpecialRoles() {
        return !isSmallBlind() && !isBigBlind() && !isButton();
    }

    public boolean isNotInGame() {
        final StateType stateType = this.settings.getStateType();
        return stateType == null || stateType == StateType.AFK || stateType == StateType.LEAVE;
    }


    public StateType getStateType(){
        return this.settings.getStateType();
    }

    public void addCards(List<CardEntity> cards) {
        this.settings.setCards(new ArrayList<>(cards));
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

    public ChipsCountEntity getChipsCount(){
        return this.settings.getChipsCount();
    }

    public List<CardEntity> getCards(){
        return this.settings.getCards();
    }

    public String getGameName(){
        return this.settings.getGameName();
    }

    public void setStateType(StateType stateType){
        this.settings.setStateType(stateType);
    }

    public Long getTimeBank(){
        return this.settings.getTimeBank();
    }

    public void setTimeBank(Long timeBank){
        this.settings.setTimeBank(timeBank);
    }

    public void setActive(boolean isActive){
        this.settings.setActive(isActive);
    }

    public void setGame(GameEntity game){
        this.settings.setGame(game);
    }

    public void setGameName(String gameName){
        this.settings.setGameName(gameName);
    }

    public RoleType getRoleType(){
        return this.settings.getRoleType();
    }

    public PlayerEntity copy() {
        return PlayerEntity
                .builder()
                .name(name)
                .action(action)
                .settings(PlayerSettingsEntity.builder()
                        .active(settings.isActive())
                        .cards(settings.getCards())
                        .chipsCount(settings.getChipsCount())
                        .game(settings.getGame())
                        .actions(settings.getActions())
                        .gameName(settings.getGameName())
                        .timeBank(settings.getTimeBank())
                        .roleType(settings.getRoleType())
                        .stateType(settings.getStateType())
                        .build())
                .build();
    }

}
