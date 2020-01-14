package ru.sm.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.holdem.Fold;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.holdem.ActionServiceHoldem;
import ru.sm.poker.util.SortUtil;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderActionService implements OrderService {

    private final ActionServiceHoldem actionServiceHoldem;

    @Override
    public void start(RoundSettingsDTO roundSettingsDTO, List<Player> filteredPlayers) {

        final List<Player> sortedPlayers = roundSettingsDTO.getStageType() == StageType.PREFLOP ?
                SortUtil.sortPreflop(roundSettingsDTO.getPlayers()) : SortUtil.sortPostflop(roundSettingsDTO.getPlayers());

        sortedPlayers.forEach(player -> {
            if (!(player.getAction() instanceof Fold) && player.getStateType() != StateType.AFK) {
                if (filteredPlayers.size() > 0) {
                    if (filteredPlayers.stream().anyMatch(fp -> fp.equals(player))) {
                        actionServiceHoldem.setAction(player, roundSettingsDTO);
                    }
                } else {
                    actionServiceHoldem.setAction(player, roundSettingsDTO);
                }
            }
        });

        final List<Player> listWithNotSameCalls = getListWithNotSameCalls(roundSettingsDTO);
        if (listWithNotSameCalls.size() != 0) {
            start(roundSettingsDTO, listWithNotSameCalls);
        }
    }

    private List<Player> getListWithNotSameCalls(RoundSettingsDTO roundSettingsDTO) {
        final List<Player> playersForCall = new ArrayList<>();

        for (Player player : roundSettingsDTO.getPlayers()) {
            if (player.getAction() instanceof CountAction) {
                final CountAction countAction = (CountAction) player.getAction();

                if (countAction.getCount() != roundSettingsDTO.getLastBet()) {
                    if (roundSettingsDTO.getStageType() == StageType.PREFLOP) {
                        if (player.isSmallBlind() && countAction.getCount() + roundSettingsDTO.getSmallBlindBet() == roundSettingsDTO.getLastBet()) {
                            continue;
                        }
                        if (player.isBigBlind() && roundSettingsDTO.getLastBet() == roundSettingsDTO.getBigBlindBet()) {
                            continue;
                        }
                        if (player.isBigBlind() && roundSettingsDTO.getBigBlindBet() + countAction.getCount() == roundSettingsDTO.getLastBet()) {
                            continue;
                        }
                    }

                    log.info("last bet:" + roundSettingsDTO.getLastBet() + " count bet:" + countAction.getCount() + " : " + player.getName());
                    player.setAction(new Wait());
                    playersForCall.add(player);
                }

            }
        }

        return playersForCall;
    }

}
