package ru.sm.poker.service.holdem;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.Player;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.action.Wait;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WaitActionService {

    @Setter
    private ActionServiceHoldem actionServiceHoldem;

    public void waitPlayerAction(Player player, List<Player> players, RoundSettingsDTO roundSettingsDTO) {
        while (true) {
            if (checkAllAfk(players)) {
                break;
            }
            if (player.getAction().getClass() != Wait.class) {
                actionServiceHoldem.parseAction(player, roundSettingsDTO);
                break;
            }
        }
    }

    private boolean checkAllAfk(List<Player> players) {
        return players.stream()
                .allMatch(player -> player.getStateType() == StateType.AFK);
    }

}