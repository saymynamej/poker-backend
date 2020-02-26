package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.Player;
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
        final List<Player> sortedPlayers = getPlayersInGame(
                sort(
                        holdemRoundSettingsDTO.getPlayers(),
                        holdemRoundSettingsDTO.getStageType()
                )
        );

        boolean isFirstStart = true;
        boolean isSkipNext = false;

        while (true) {
            if (playersInAllIn(holdemRoundSettingsDTO)) {
                securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(holdemRoundSettingsDTO);
                break;
            }
            if (allPlayersInGameHaveSameCountOfBet(holdemRoundSettingsDTO) && holdemRoundSettingsDTO.getLastBet() != 0) {
                break;
            }
            if (playersCheck(sortedPlayers)) {
                break;
            }
            if (isOnePlayerLeft(sortedPlayers)) {
                isSkipNext = true;
                break;
            }

            for (Player player : sortedPlayers) {
                if (player.getStateType() == null || player.getStateType() == StateType.AFK || player.getStateType() == StateType.LEAVE) {
                    continue;
                }
                if (isNotEnoughChips(player)) {
                    continue;
                }
                if (isOnePlayerLeft(sortedPlayers)) {
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


    private List<Player> sort(List<Player> players, StageType stageType) {
        return stageType == StageType.PREFLOP ? sortPreflop(players) : sortPostflop(players);
    }

    private boolean isNotEnoughChips(Player player) {
        return player.getChipsCount() == 0;
    }

    private boolean playersInAllIn(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        return getPlayersInGame(holdemRoundSettingsDTO.getPlayers()).stream()
                .allMatch(playerInAllIn());
    }

    private boolean playersCheck(List<Player> players) {
        return players.stream()
                .filter(playerInAllIn().negate())
                .allMatch(playersHasCheck());
    }

    private boolean isOnePlayerLeft(List<Player> players) {
        return players.stream()
                .filter(player -> player.getAction().getActionType() == ActionType.FOLD && player.getStateType() == StateType.IN_GAME)
                .count() == players.size() - 1;
    }

}
