package ru.sm.poker.model.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sm.poker.enums.ActionType;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bet implements Action {

    private long count;

    @Override
    public ActionType getActionType() {
        return ActionType.BET;
    }

}
