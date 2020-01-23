package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.action.holdem.Check;
import ru.sm.poker.action.holdem.Fold;
import ru.sm.poker.dto.RoundSettingsDTO;
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

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderActionService implements OrderService {

    private final ActionService actionServiceHoldem;

    @Override
    public void start(RoundSettingsDTO roundSettingsDTO) {
        final List<Player> sortedPlayers = getPlayersInGame(roundSettingsDTO.getStageType() == StageType.PREFLOP ?
                sortPreflop(roundSettingsDTO.getPlayers()) : sortPostflop(roundSettingsDTO.getPlayers()).stream()
                .filter(player -> player.getStateType() != StateType.AFK)
                .collect(Collectors.toList()));

        setAfkPlayersWithoutChips(sortedPlayers);

        boolean isFirstStart = true;
        boolean isWork = true;

        while (!allPlayersInGameHaveSameCountOfBet(roundSettingsDTO) || roundSettingsDTO.getLastBet() == 0 && isWork) {
            if (allChecks(sortedPlayers)) {
                break;
            }
            if (allPlayersInAllIn(sortedPlayers)) {
                break;
            }
            for (Player player : sortedPlayers) {
                if (player.getStateType() == null || player.getStateType() == StateType.AFK) {
                    continue;
                }
                if (isOnePlayerLeft(sortedPlayers)) {
                    isWork = false;
                    break;
                }
                if (player.getAction() instanceof Fold) {
                    continue;
                }
                if (sumAllHistoryBets(roundSettingsDTO, player) == roundSettingsDTO.getLastBet()) {
                    if (!isFirstStart) {
                        continue;
                    }
                }
                actionServiceHoldem.waitUntilPlayerWillHasAction(player, roundSettingsDTO);
            }
            isFirstStart = false;
        }
    }

    private void setAfkPlayersWithoutChips(List<Player> sortedPlayers) {
        sortedPlayers.forEach(player -> {
            if (player.getChipsCount() == 0) {
                player.setStateType(StateType.AFK);
            }
        });
    }

    private boolean allPlayersInAllIn(List<Player> players) {
        return getPlayersInGame(players).stream()
                .allMatch(player -> player.getChipsCount() == 0 && player.getAction() != null);
    }

    private boolean isOnePlayerLeft(List<Player> players) {
        return players.stream()
                .filter(player -> !(player.getAction() instanceof Fold) && player.getStateType() == StateType.IN_GAME)
                .count() == 1;
    }

    private boolean allChecks(List<Player> players) {
        return players.stream()
                .allMatch(player -> player.getAction() instanceof Check);
    }
}
