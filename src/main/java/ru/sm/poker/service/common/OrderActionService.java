package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.OrderService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.sm.poker.util.HistoryUtil.allPlayersInGameHaveSameCountOfBet;
import static ru.sm.poker.util.HistoryUtil.sumAllHistoryBets;
import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;
import static ru.sm.poker.util.SortUtil.sortPostflop;
import static ru.sm.poker.util.SortUtil.sortPreflop;
import static ru.sm.poker.util.StreamUtil.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderActionService implements OrderService {

    private final ActionService actionServiceHoldem;

    @Override
    public void start(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final List<PlayerDTO> sortedPlayerDTOS = getPlayersInGame(sort(
                holdemRoundSettingsDTO.getPlayers(),
                holdemRoundSettingsDTO.getStageType())).stream()
                .filter(playerIsNotAfk())
                .collect(Collectors.toList());


        boolean isFirstStart = true;
        boolean isWork = true;

        while (true) {
            if (allPlayersInGameHaveSameCountOfBet(holdemRoundSettingsDTO) && holdemRoundSettingsDTO.getLastBet() != 0 && isWork) {
                break;
            }

            if (allChecks(sortedPlayerDTOS)) {
                break;
            }

            if (allPlayersInAllIn(holdemRoundSettingsDTO)) {
                break;
            }

            for (PlayerDTO playerDTO : sortedPlayerDTOS) {
                if (playerDTO.getStateType() == null || playerDTO.getStateType() == StateType.AFK || playerDTO.getStateType() == StateType.LEAVE) {
                    continue;
                }
                if (playersHasNotChips(playerDTO)) {
                    continue;
                }
                if (isOnePlayerLeft(sortedPlayerDTOS)) {
                    isWork = false;
                    break;
                }
                if (playerDTO.getAction().getActionType() == ActionType.FOLD) {
                    continue;
                }
                if (sumAllHistoryBets(holdemRoundSettingsDTO, playerDTO) == holdemRoundSettingsDTO.getLastBet()) {
                    if (!isFirstStart) {
                        continue;
                    }
                }
                actionServiceHoldem.waitUntilPlayerWillHasAction(playerDTO, holdemRoundSettingsDTO);
            }
            isFirstStart = false;
        }
    }


    private List<PlayerDTO> sort(List<PlayerDTO> playerDTOS, StageType stageType) {
        return stageType == StageType.PREFLOP ? sortPreflop(playerDTOS) : sortPostflop(playerDTOS);
    }

    private boolean playersHasNotChips(PlayerDTO playerDTO) {
        return playerDTO.getChipsCount() == 0;
    }

    private boolean allPlayersInAllIn(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        return getPlayersInGame(holdemRoundSettingsDTO.getPlayers()).stream()
                .allMatch(playerInAllIn());

    }

    private boolean allChecks(List<PlayerDTO> playerDTOS) {
        return playerDTOS.stream()
                .filter(playerInAllIn().negate())
                .allMatch(playersHasCheck());
    }

    private boolean isOnePlayerLeft(List<PlayerDTO> playerDTOS) {
        return playerDTOS.stream()
                .filter(player -> player.getAction().getActionType() == ActionType.FOLD && player.getStateType() == StateType.IN_GAME)
                .count() == playerDTOS.size() - 1;
    }

}
