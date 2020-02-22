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

import static ru.sm.poker.util.HistoryUtil.allPlayersInGameHaveSameCountOfBet;
import static ru.sm.poker.util.HistoryUtil.sumStageHistoryBets;
import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;
import static ru.sm.poker.util.SortUtil.sortPostflop;
import static ru.sm.poker.util.SortUtil.sortPreflop;
import static ru.sm.poker.util.StreamUtil.playerInAllIn;
import static ru.sm.poker.util.StreamUtil.playersHasCheck;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderActionService implements OrderService {

    private final ActionService actionServiceHoldem;
    private final SecurityNotificationService securityNotificationService;

    @Override
    public boolean start(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final List<PlayerDTO> sortedPlayerDTOS = getPlayersInGame(
                sort(
                        holdemRoundSettingsDTO.getPlayers(),
                        holdemRoundSettingsDTO.getStageType()
                )
        );

        boolean isFirstStart = true;
        boolean isSkipNext = false;

        while (true) {
            if (allPlayersInAllIn(holdemRoundSettingsDTO)) {
                securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(holdemRoundSettingsDTO);
                break;
            }
            if (allPlayersInGameHaveSameCountOfBet(holdemRoundSettingsDTO) && holdemRoundSettingsDTO.getLastBet() != 0) {
                break;
            }
            if (allChecks(sortedPlayerDTOS)) {
                break;
            }
            if (isOnePlayerLeft(sortedPlayerDTOS)) {
                isSkipNext = true;
                break;
            }

            for (PlayerDTO player : sortedPlayerDTOS) {
                if (player.getStateType() == null || player.getStateType() == StateType.AFK || player.getStateType() == StateType.LEAVE) {
                    continue;
                }
                if (playersHasNotChips(player)) {
                    continue;
                }
                if (isOnePlayerLeft(sortedPlayerDTOS)) {
                    isSkipNext = true;
                    break;
                }
                if (player.getAction().getActionType() == ActionType.FOLD) {
                    continue;
                }
                if (sumStageHistoryBets(holdemRoundSettingsDTO, player) == holdemRoundSettingsDTO.getLastBet()) {
                    if (!isFirstStart) {
                        continue;
                    }
                }
                actionServiceHoldem.waitUntilPlayerWillHasAction(player, holdemRoundSettingsDTO);
            }
            isFirstStart = false;
        }
        return isSkipNext;
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
