package ru.sm.poker.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.CountAction;
import ru.sm.poker.model.action.Fold;
import ru.sm.poker.model.action.Wait;
import ru.sm.poker.service.holdem.ActionServiceHoldem;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class HoldemPipeline implements Pipeline {

    private final ActionServiceHoldem actionServiceHoldem;

    @Override
    public void start(RoundSettingsDTO roundSettingsDTO, List<Player> filteredPlayers) {
        for (Player player : roundSettingsDTO.getPlayers()) {
            if (!(player.getAction() instanceof Fold) && player.getStateType() != StateType.AFK) {
                if (filteredPlayers.size() > 0) {
                    if (filteredPlayers.stream().anyMatch(fp -> fp.equals(player))) {
                        actionServiceHoldem.setActions(player, roundSettingsDTO);
                    }
                } else {
                    actionServiceHoldem.setActions(player, roundSettingsDTO);
                }
            }
        }


        final List<Player> listWithNotSameCalls = getListWithNotSameCalls(roundSettingsDTO.getPlayers(), roundSettingsDTO.getLastBet());
        if (listWithNotSameCalls.size() != 0) {
            start(roundSettingsDTO, listWithNotSameCalls);
        }

    }


    private List<Player> getListWithNotSameCalls(List<Player> players, long lastBet) {
        final List<Player> playersForCall = new ArrayList<>();

        players.forEach(player -> {
            if (player.getAction() instanceof CountAction) {
                final CountAction countAction = (CountAction) player.getAction();
                if (countAction.getCount() != lastBet) {
                    log.info("last bet:" + lastBet + " count bet:" + countAction.getCount() + " : " + player.getName());
                    player.setAction(new Wait(player.getGameName()));
                    playersForCall.add(player);
                }
            }
        });

        return playersForCall;
    }

}
