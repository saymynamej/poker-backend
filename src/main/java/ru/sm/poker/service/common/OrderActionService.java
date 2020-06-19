package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.OrderService;

import java.util.List;

import static ru.sm.poker.util.HistoryUtil.allPlayersInGameHaveSameCountOfBet;
import static ru.sm.poker.util.HistoryUtil.sumStageHistoryBets;
import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;
import static ru.sm.poker.util.SortUtil.sort;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderActionService implements OrderService {
    private final ActionService actionServiceHoldem;

    @Override
    public boolean start(HoldemRoundSettingsDTO holdemRoundSettings) {
        final List<PlayerDTO> sortedPlayers = getPlayersInGame(
                sort(holdemRoundSettings.getPlayers(), holdemRoundSettings.getStageType())
        );

        boolean isFirstStart = true;
        boolean isSkipNext = false;

        while (true) {
            if (holdemRoundSettings.playersInAllIn()) {
                break;
            }

            if (allPlayersInGameHaveSameCountOfBet(holdemRoundSettings) && holdemRoundSettings.lastBetIsNotZero()) {
                break;
            }

            if (holdemRoundSettings.playersCheck()) {
                break;
            }

            if (holdemRoundSettings.isOnePlayerLeft() || holdemRoundSettings.isOnePlayerWhoHasChips()) {
                isSkipNext = true;
                break;
            }

            for (PlayerDTO player : sortedPlayers) {
                if (player.isNotInGame()) {
                    continue;
                }
                if (player.hasZeroChips()) {
                    continue;
                }
                if (holdemRoundSettings.isOnePlayerLeft()) {
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

}
