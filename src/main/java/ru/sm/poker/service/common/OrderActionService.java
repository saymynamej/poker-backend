package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.OrderService;

import java.util.List;

import static ru.sm.poker.util.HistoryUtil.allPlayersInGameHaveSameCountOfBet;
import static ru.sm.poker.util.HistoryUtil.sumStageHistoryBets;
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
    public boolean start(HoldemRoundSettings holdemRoundSettings) {
        final List<Player> sortedPlayers = getPlayersInGame(
                sort(holdemRoundSettings.getPlayers(), holdemRoundSettings.getStageType())
        );

        boolean isFirstStart = true;
        boolean isSkipNext = false;

        while (true) {
            if (playersInAllIn(sortedPlayers)) {
                break;
            }

            if (allPlayersInGameHaveSameCountOfBet(holdemRoundSettings) && lastBetIsNotZero(holdemRoundSettings.getLastBet())) {
                break;
            }

            if (playersCheck(sortedPlayers)) {
                break;
            }

            if (isOnePlayerLeft(sortedPlayers) || isOnePlayerWhoHasChips(sortedPlayers)) {
                isSkipNext = true;
                break;
            }

            for (Player player : sortedPlayers) {
                if (player.isNotInGame()) {
                    continue;
                }
                if (player.hasZeroChips()) {
                    continue;
                }
                if (isOnePlayerLeft(sortedPlayers)) {
                    isSkipNext = true;
                    break;
                }
                if (player.isFolded()) {
                    continue;
                }
                if (sumStageHistoryBets(holdemRoundSettings, player) == holdemRoundSettings.getLastBet() && !isFirstStart) {
                        continue;
                }
                actionServiceHoldem.waitUntilPlayerWillHasAction(player, holdemRoundSettings);
            }
            isFirstStart = false;
        }
        return isSkipNext;
    }

    private boolean lastBetIsNotZero(Long lastBet) {
        return lastBet != 0;
    }

    private List<Player> sort(List<Player> players, StageType stageType) {
        return stageType == StageType.PREFLOP ? sortPreflop(players) : sortPostflop(players);
    }

    private boolean playersInAllIn(List<Player> players) {
        return getPlayersInGame(players)
                .stream()
                .filter(playerFolded().negate())
                .allMatch(playerInAllIn());
    }

    private boolean playersCheck(List<Player> players) {
        return players.stream()
                .filter(playerFolded().negate())
                .allMatch(playersHasCheck());
    }

    private boolean isOnePlayerLeft(List<Player> players) {
        return players.stream()
                .filter(playerFolded().negate())
                .count() == 1;
    }

    private boolean isOnePlayerWhoHasChips(List<Player> players){
        return players.stream()
                .filter(player -> player.getAction().getActionType() != ActionType.ALLIN && player.getChipsCount() != 0)
                .count() == 1;
    }

}
