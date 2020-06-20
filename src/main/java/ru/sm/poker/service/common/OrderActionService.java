package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.OrderService;

import java.util.List;

import static ru.sm.poker.util.HistoryUtil.canMoveNext;
import static ru.sm.poker.util.HistoryUtil.canMoveNextAndStageRiver;
import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;
import static ru.sm.poker.util.SortUtil.sort;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderActionService implements OrderService {
    private final ActionService actionServiceHoldem;
    private final SecurityNotificationService securityNotificationService;

    @Override
    public boolean start(HoldemRoundSettingsDTO holdemRoundSettings) {
        final List<PlayerDTO> sortedPlayers = getPlayersInGame(
                sort(holdemRoundSettings.getPlayers(), holdemRoundSettings.getStageType())
        );

        while (true) {
            if (holdemRoundSettings.playersInAllIn()) {
                securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(holdemRoundSettings);
                return false;
            }
            if (canMoveNextAndStageRiver(holdemRoundSettings)) {
                securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(holdemRoundSettings);
                return false;
            }
            if (canMoveNext(holdemRoundSettings)) {
                return false;
            }
            for (PlayerDTO player : getPlayersInGame(sortedPlayers)) {
                if (player.isNotFirstMoveOnBigBlind() && canMoveNext(holdemRoundSettings)) {
                    return false;
                }
                //THIS IS FOR HU
                if (holdemRoundSettings.getStageType() != StageType.PREFLOP && canMoveNext(holdemRoundSettings)){
                    return false;
                }
                if (holdemRoundSettings.isOnePlayerLeft()) {
                    return true;
                }
                if (player.isInAllIn()) {
                    continue;
                }
                actionServiceHoldem.waitUntilPlayerWillHasAction(player, holdemRoundSettings);
            }
        }
    }
}
