package ru.sm.poker.model.action;

import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;

public class Check implements Action {

    private final String gameName;

    public Check(String gameName){
        this.gameName = gameName;
    }

    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public ActionType getActionType() {
        return ActionType.CHECK;
    }

    @Override
    public void doAction(Player player) {

    }
}
