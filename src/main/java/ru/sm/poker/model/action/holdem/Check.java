package ru.sm.poker.model.action.holdem;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.ExecutableAction;
import ru.sm.poker.service.ActionService;

public class Check implements ExecutableAction {

    private final String gameName;

    public Check(String gameName) {
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
    public void doAction(RoundSettingsDTO roundSettingsDTO, Player player, ActionService actionService) {
        if (!(player.isBigBlind() && roundSettingsDTO.getBigBlindBet() == roundSettingsDTO.getLastBet())) {
            actionService.waitPlayerAction(player, roundSettingsDTO);
        }
    }
}
