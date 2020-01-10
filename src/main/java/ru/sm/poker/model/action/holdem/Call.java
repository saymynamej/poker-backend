package ru.sm.poker.model.action.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.ExecutableAction;
import ru.sm.poker.service.ActionService;

@Getter
@Setter
@RequiredArgsConstructor
public class Call implements ExecutableAction {

    private final long count;
    private final String gameName;

    @Override
    public ActionType getActionType() {
        return ActionType.CALL;
    }

    @Override
    public void doAction(RoundSettingsDTO roundSettingsDTO, Player player, ActionService actionService) {
        if (getCount() != roundSettingsDTO.getLastBet()) {
            if (player.isSmallBlind()) {
                final long bet = getCount() + getCount();
                if (bet == roundSettingsDTO.getBigBlindBet()) {
                    actionService.removeChipsPlayerAndAddToBank(player, getCount(), roundSettingsDTO);
                    actionService.setLastBet(roundSettingsDTO, bet);
                    return;
                }
            } else {
                actionService.setPlayerWait(player);
                actionService.waitPlayerAction(player, roundSettingsDTO);
            }
            actionService.setPlayerWait(player);
            actionService.waitPlayerAction(player, roundSettingsDTO);
            return;
        }

        actionService.removeChipsPlayerAndAddToBank(player, getCount(), roundSettingsDTO);
        actionService.setLastBet(roundSettingsDTO, getCount());
    }
}
