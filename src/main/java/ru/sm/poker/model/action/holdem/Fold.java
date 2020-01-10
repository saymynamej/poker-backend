package ru.sm.poker.model.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Action;
import ru.sm.poker.model.action.ExecutableAction;
import ru.sm.poker.service.ActionService;

@RequiredArgsConstructor
@Getter
public class Fold implements Action {

    private final String gameName;

    @Override
    public ActionType getActionType() {
        return ActionType.FOLD;
    }
}
