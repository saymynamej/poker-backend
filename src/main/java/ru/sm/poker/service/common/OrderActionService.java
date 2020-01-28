package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.Player;
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
        final List<Player> sortedPlayers = getPlayersInGame(sort(
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

            if (allChecks(sortedPlayers)) {
                break;
            }

            if (allPlayersInAllIn(holdemRoundSettingsDTO)) {
                break;
            }

            for (Player player : sortedPlayers) {
                if (player.getStateType() == null || player.getStateType() == StateType.AFK || player.getStateType() == StateType.LEAVE) {
                    continue;
                }
                if (playersHasNotChips(player)) {
                    continue;
                }
                if (isOnePlayerLeft(sortedPlayers)) {
                    isWork = false;
                    break;
                }
                if (player.getAction().getActionType() == ActionType.FOLD) {
                    continue;
                }
                if (sumAllHistoryBets(holdemRoundSettingsDTO, player) == holdemRoundSettingsDTO.getLastBet()) {
                    if (!isFirstStart) {
                        continue;
                    }
                }
                actionServiceHoldem.waitUntilPlayerWillHasAction(player, holdemRoundSettingsDTO);
            }
            isFirstStart = false;
        }
    }


    private List<Player> sort(List<Player> players, StageType stageType) {
        return stageType == StageType.PREFLOP ? sortPreflop(players) : sortPostflop(players);
    }

    private boolean playersHasNotChips(Player player) {
        return player.getChipsCount() == 0;
    }

    private boolean allPlayersInAllIn(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        return getPlayersInGame(holdemRoundSettingsDTO.getPlayers()).stream()
                .allMatch(playerInAllIn());

    }

    private boolean allChecks(List<Player> players) {
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
