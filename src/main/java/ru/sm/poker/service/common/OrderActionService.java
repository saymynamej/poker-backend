package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.action.holdem.Fold;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.util.SortUtil;

import java.util.ArrayList;
import java.util.List;

import static ru.sm.poker.util.HistoryUtil.allActionsEqualsLastBetPostFlop;
import static ru.sm.poker.util.HistoryUtil.allActionsEqualsLastBetPreflop;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderActionService implements OrderService {

    private final ActionService actionServiceHoldem;

    @Override
    public void start(RoundSettingsDTO roundSettingsDTO, List<Player> filteredPlayers) {

        final List<Player> sortedPlayers = roundSettingsDTO.getStageType() == StageType.PREFLOP ?
                SortUtil.sortPreflop(roundSettingsDTO.getPlayers()) : SortUtil.sortPostflop(roundSettingsDTO.getPlayers());

        for (Player player : sortedPlayers) {
            if (!(player.getAction() instanceof Fold) && player.getStateType() != StateType.AFK) {
                if (filteredPlayers.size() > 0) {
                    if (filteredPlayers.stream().anyMatch(fp -> fp.equals(player))) {
                        actionServiceHoldem.waitUntilPlayerWillHasAction(player, roundSettingsDTO);
                    }
                } else {
                    actionServiceHoldem.waitUntilPlayerWillHasAction(player, roundSettingsDTO);
                }
            }
        }

        final List<Player> listWithNotSameCalls = getListWithNotSameCalls(roundSettingsDTO);
        if (listWithNotSameCalls.size() != 0) {
            start(roundSettingsDTO, listWithNotSameCalls);
        }
    }

    private List<Player> getListWithNotSameCalls(RoundSettingsDTO roundSettingsDTO) {
        final List<Player> playersForCall = new ArrayList<>();
        for (Player player : roundSettingsDTO.getPlayers()) {
            if (player.getAction() instanceof Fold){
                continue;
            }
            if (roundSettingsDTO.getStageType() == StageType.PREFLOP && !allActionsEqualsLastBetPreflop(roundSettingsDTO, player)) {
                player.setAction(new Wait());
                playersForCall.add(player);
            }
            if (roundSettingsDTO.getStageType() != StageType.PREFLOP && !allActionsEqualsLastBetPostFlop(roundSettingsDTO, player)) {
                player.setAction(new Wait());
                playersForCall.add(player);
            }
        }
        return playersForCall;
    }

}
